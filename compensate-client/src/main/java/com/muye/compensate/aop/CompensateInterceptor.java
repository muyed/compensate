package com.muye.compensate.aop;

import com.alibaba.dubbo.config.annotation.Reference;
import com.muye.compensate.DTO.CompensateDTO;
import com.muye.compensate.annotation.Compensate;
import com.muye.compensate.api.CompensateApiService;
import com.muye.compensate.codec.ParamsCodec;
import com.muye.compensate.constant.CompensateConstant;
import com.muye.compensate.constant.CompensateStatusEnum;
import com.muye.compensate.exception.CompensateException;
import com.muye.compensate.result.Result;
import com.muye.compensate.util.ThreadLocalUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.stream.Stream;

/**
 * created by dahan at 2018/8/1
 *
 * <p>
 *     1. 根据当前线程是否为补偿线程判断方法是补偿执行还是正常执行
 *     2. 如果是补偿执行
 *      2-1. 将Compensate表状态更新为{@link com.muye.compensate.constant.CompensateStatusEnum#COMPENSATING}
 *      2-2. 执行方法
 *      2-3. 执行成功,物理删除Compensate表,新增CompensateCase表和CompensateHist表,status:{@link
 *           com.muye.compensate.constant.CompensateStatusEnum#SUCCESSFUL}
 *      2-4. 执行失败,如果当前补偿次数等于最大补偿次数,物理删除Compensate表,新增CompensateCase表和CompensateHist表,status:{@link
 *           com.muye.compensate.constant.CompensateStatusEnum#FAILED};如果当前补偿次数小于最大补偿次数,更新Compensate
 *           表,currentNum+1,status:{@link com.muye.compensate.constant.CompensateStatusEnum#WAIT},并且记录
 *           CompensateCase表
 *     3. 如果是非补偿执行
 *      3-1. 执行方法
 *      3-2. 如果执行失败,增加Compensate表记录
 *
 *     异常情况：
 *     补偿执行时,由于分布式事物问题导致的Compensate表状态没有更新,状态会一直为COMPENSATING,后续不会再次补偿;需要关注下Compensate长
 *     时间状态为COMPENSATING的记录,人工确定下是成功补偿还是失败补偿了。
 * </p>
 *
 * @see com.muye.compensate.work.CompensateWork
 */
@Aspect
public class CompensateInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompensateInterceptor.class);


    @Reference(version = "1.0.0")
    private CompensateApiService compensateApiService;

    @Around("@annotation(compensate)")
    public Object interceptor(ProceedingJoinPoint point, Compensate compensate){

        //是否为补偿线程在执行
        boolean compensating = CompensateConstant.COMPENSATE_THREAD_NAME.equals(Thread.currentThread().getName());

        //补偿执行
        if (compensating) {
            try {
                CompensateDTO compensateDTO = ThreadLocalUtil.get();

                if (compensateDTO == null) {
                    LOGGER.error("compensateDTO cant be null");
                    throw new CompensateException("compensateDTO cant be null");
                }

                LOGGER.info("compensating[id:{}]: {}_{}_{}", compensateDTO.getId(), compensateDTO.getAppName(),
                        compensateDTO.getClassFullName(), compensateDTO.getMethodName());

                //尝试获取补偿资格
                Result<Boolean> tryCompensating = compensateApiService.tryCompensate(compensateDTO.getId());

                if (!tryCompensating.isSuccess() || !tryCompensating.getData()) {
                    LOGGER.warn("compensating[id:{}]: tryCompensate failed", compensateDTO.getId());
                    throw new CompensateException("update status to COMPENSATING failed");
                }

                try {
                    Object result = point.proceed();
                    //执行成功回调
                    try {
                        Result callback = compensateApiService.compensateSuccessfulCallBack(compensateDTO.getId());
                        if (callback.isSuccess()) {
                            LOGGER.info("compensating[id:{}]:successful,callback successful");
                        } else {
                            LOGGER.error("compensating[id:{}]:successful,callback failed,errMsg:",callback.getErrMsg());
                        }
                    } catch (Exception e){
                        LOGGER.error("compensating[id:{}]:successful, invoke dubbo failed,", compensateDTO.getId(), e);
                    }
                    return result;
                }
                //补偿失败
                catch (Throwable e) {
                    LOGGER.warn("compensate[id:{}]failed, running follow up operation", compensateDTO.getId(), e);
                    //执行补偿失败回调
                    try {
                        Result callback =
                                compensateApiService.compensateFailedCallBack(compensateDTO.getId(), e.getMessage());

                        if (callback.isSuccess()){
                            LOGGER.info("compensating[id:{}]:failed,callback successful");
                        } else {
                            LOGGER.error("compensating[id:{}]:failed,callback failed,errMsg:", callback.getErrMsg());
                        }
                    } catch (Exception ex) {
                        LOGGER.error("compensating[id:{}]:failed, invoke dubbo failed", compensateDTO.getId(), ex);
                    }
                    throw new CompensateException(e);
                }
            } finally {
                ThreadLocalUtil.remove();
            }
        }
        // 非补偿执行
        else {
            String appName = System.getProperty("project.name");
            Method method = ((MethodSignature)point.getSignature()).getMethod();
            String methodName = method.getName();
            String classFullName = method.getDeclaringClass().getName();
            Object[] params =  point.getArgs();
            Class[] paramTypes = method.getParameterTypes();

            LOGGER.info("compensateInterceptor -> {}#{}", classFullName, methodName);

            try {
                return point.proceed();
            } catch (Throwable e) {
                //执行失败 添加到补偿表
                LOGGER.warn("invoke compensate method failed", e);
                try {
                    if (StringUtils.isEmpty(appName)) {
                        throw new CompensateException("project.name is null");
                    }

                    ParamsCodec paramsCodec = compensate.codec().newInstance();
                    String paramsBinary = paramsCodec.encode(params);

                    CompensateDTO compensateDTO = new CompensateDTO();
                    compensateDTO.setAppName(appName);
                    compensateDTO.setClassFullName(classFullName);
                    compensateDTO.setStatus(CompensateStatusEnum.WAIT.getStatus());
                    compensateDTO.setCurrentNum(0);
                    compensateDTO.setCodecProtocol(paramsCodec.getCode());
                    compensateDTO.setMaxNum(compensate.maxNum());
                    compensateDTO.setMethodName(methodName);
                    compensateDTO.setParams(paramsBinary);

                    if (paramTypes != null && paramTypes.length > 0) {
                        String paramTypesStr = Stream.of(paramTypes)
                                .map(paramType -> paramType.getName())
                                .reduce((a, b) -> a + "," + b)
                                .get();

                        compensateDTO.setParamTypes(paramTypesStr);
                    }

                    Result save = compensateApiService.save(compensateDTO);

                    if (save.isSuccess()) {
                        LOGGER.info("add compensate successful");
                    }
                } catch (Exception ex) {
                    LOGGER.error("save compensate failed", ex);
                }

                throw new CompensateException(e);
            }
        }
    }
}
