package com.muye.compensate.work;

import com.alibaba.dubbo.config.annotation.Reference;
import com.muye.compensate.DTO.CompensateDTO;
import com.muye.compensate.api.CompensateApiService;
import com.muye.compensate.codec.ParamsCodec;
import com.muye.compensate.constant.CompensateStatusEnum;
import com.muye.compensate.exception.CompensateException;
import com.muye.compensate.query.CompensateQuery;
import com.muye.compensate.result.Result;
import com.muye.compensate.util.ThreadLocalUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * created by dahan at 2018/8/2
 *
 * @see com.muye.compensate.aop.CompensateInterceptor
 */
public class CompensateWork implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompensateWork.class);

    private final CompensateThreadFactory compensateThreadFactory = new CompensateThreadFactory();

    private ApplicationContext applicationContext;

    @Reference(version = "1.0.0")
    private CompensateApiService compensateApiService;

    @Value("${compensate.enable:true}")
    private Boolean enable;

    private void work(){

        compensateThreadFactory.newThread(() -> {

            if (!enable) {
                LOGGER.warn("compensate enable is {}, compensate closed", enable);
                return;
            }

            for (;;) {
                try {
                    Thread.sleep(1000l * 60);    //每次执行间隔1分钟
                    LOGGER.info("working...");

                    String appName = System.getProperty("project.name");
                    if (StringUtils.isEmpty(appName)) {
                        throw new CompensateException("project.name cant be null");
                    }
                    CompensateQuery query = new CompensateQuery();
                    query.setAppName(appName);
                    query.setStatus(CompensateStatusEnum.WAIT.getStatus());
                    Result<List<CompensateDTO>> result = compensateApiService.query(query);
                    if (!result.isSuccess()) {
                        throw new CompensateException(result.getErrMsg());
                    }

                    List<CompensateDTO> list = result.getData();
                    if (CollectionUtils.isEmpty(list)) {
                        LOGGER.info("none compensate");
                        continue;
                    }

                    list.forEach(this::compensate);

                } catch (Exception e) {
                    LOGGER.error("work failed", e);
                }
            }
        }).start();
    }

    /**
     * 补偿
     * @param compensateDTO
     */
    private void compensate(CompensateDTO compensateDTO){
        LOGGER.info("compensate[id:{}] -> {}#{}", compensateDTO.getId(), compensateDTO.getClassFullName(),
                compensateDTO.getMethodName());

        try {
            ThreadLocalUtil.set(compensateDTO);
            ParamsCodec paramsCodec = ParamsCodec.getByCode(compensateDTO.getCodecProtocol());

            String paramTypesStr = compensateDTO.getParamTypes();

            Object[] params = null;
            Class[] paramsTypes = null;

            if (StringUtils.hasLength(paramTypesStr)) {
                String[] paramTypesArray = paramTypesStr.split(",");
                paramsTypes = new Class[paramTypesArray.length];
                for (int i = 0; i < paramsTypes.length; i++) {
                    paramsTypes[i] = Class.forName(paramTypesArray[i]);
                }

                params = paramsCodec.decode(compensateDTO.getParams());
            }

            Class type = Class.forName(compensateDTO.getClassFullName());
            Object bean = applicationContext.getBean(type);
            Method method;

            if (paramsTypes != null) {
                method = type.getMethod(compensateDTO.getMethodName(), paramsTypes);
                method.invoke(bean, params);
            } else {
                method = type.getMethod(compensateDTO.getMethodName());
                method.invoke(bean);
            }
        } catch (Exception e) {
            LOGGER.error("compensate[id:{}]failed", compensateDTO.getId(), e);
        } finally {
            ThreadLocalUtil.remove();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        work();
    }
}
