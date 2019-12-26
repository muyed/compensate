package com.muye.compensate.service.api.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.muye.compensate.DTO.CompensateCaseDTO;
import com.muye.compensate.DTO.CompensateDTO;
import com.muye.compensate.DTO.CompensateHistDTO;
import com.muye.compensate.api.CompensateApiService;
import com.muye.compensate.constant.CompensateStatusEnum;
import com.muye.compensate.exception.CompensateException;
import com.muye.compensate.exception.UnknownException;
import com.muye.compensate.query.CompensateQuery;
import com.muye.compensate.result.Result;
import com.muye.compensate.service.CompensateCaseService;
import com.muye.compensate.service.CompensateHistService;
import com.muye.compensate.service.CompensateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * created by dahan at 2018/8/2
 */
@Service
public class CompensateApiServiceImpl implements CompensateApiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompensateApiServiceImpl.class);

    @Autowired
    private CompensateService compensateService;

    @Autowired
    private CompensateCaseService compensateCaseService;

    @Autowired
    private CompensateHistService compensateHistService;

    @Override
    public Result<Boolean> tryCompensate(Long compensateId) {
        try {
            CompensateDTO compensateDTO = new CompensateDTO();
            compensateDTO.setId(compensateId);
            compensateDTO.setStatus(CompensateStatusEnum.COMPENSATING.getStatus());
            Integer i = compensateService.update(compensateDTO);
            return Result.ok(i == 1);
        } catch (CompensateException e) {
            LOGGER.error("tryCompensate err, SntException: {}", e);
            return Result.fail(e);
        } catch (Exception e) {
            LOGGER.error("tryCompensate err, e: {}", e);
            return Result.fail(new UnknownException(e));
        }
    }

    @Override
    public Result<Boolean> compensateSuccessfulCallBack(Long compensateId) {
        try {
            CompensateDTO compensateDTO = compensateService.getById(compensateId);

            //删除compensate记录
            compensateService.delete(compensateId);

            //增加case记录
            CompensateCaseDTO compensateCaseDTO = new CompensateCaseDTO();
            BeanUtils.copyProperties(compensateDTO, compensateCaseDTO);
            compensateCaseDTO.setCompensateId(compensateDTO.getId());
            compensateCaseDTO.setResult("successful");
            compensateCaseDTO.setStatus(CompensateStatusEnum.SUCCESSFUL.getStatus());
            compensateCaseService.save(compensateCaseDTO);

            //增加hist记录
            CompensateHistDTO compensateHistDTO = new CompensateHistDTO();
            BeanUtils.copyProperties(compensateDTO, compensateHistDTO);
            compensateHistDTO.setCompensateId(compensateDTO.getId());
            compensateHistDTO.setStatus(CompensateStatusEnum.SUCCESSFUL.getStatus());
            compensateHistService.save(compensateHistDTO);

            return Result.ok(true);
        } catch (CompensateException e) {
            LOGGER.error("compensateSuccessfulCallBack err, SntException: {}", e);
            return Result.fail(e);
        } catch (Exception e) {
            LOGGER.error("compensateSuccessfulCallBack err, e: {}", e);
            return Result.fail(new UnknownException(e));
        }
    }

    @Override
    public Result<Boolean> compensateFailedCallBack(Long compensateId, String failMsg) {
        try {
            CompensateDTO compensateDTO = compensateService.getById(compensateId);

            //当前补偿次数小于最大补偿次数
            if (compensateDTO.getCurrentNum().intValue() < compensateDTO.getMaxNum().intValue()) {
                Boolean updateCompemsate = false;
                Boolean saveCase = false;

                //添加case
                CompensateCaseDTO compensateCaseDTO = new CompensateCaseDTO();
                BeanUtils.copyProperties(compensateDTO, compensateCaseDTO);
                compensateCaseDTO.setStatus(CompensateStatusEnum.FAILED.getStatus());
                compensateCaseDTO.setCompensateId(compensateDTO.getId());
                compensateCaseDTO.setResult(failMsg);
                compensateCaseService.save(compensateCaseDTO);

                //更新compensate状态及补偿次数
                compensateDTO.setStatus(CompensateStatusEnum.WAIT.getStatus());
                compensateDTO.setCurrentNum(compensateDTO.getCurrentNum() + 1);
                compensateService.update(compensateDTO);
            }
            //当前补偿次数等于最大补偿次数
            else {
                //删除compensate记录
                compensateService.delete(compensateId);

                //增加case记录
                CompensateCaseDTO compensateCaseDTO = new CompensateCaseDTO();
                BeanUtils.copyProperties(compensateDTO, compensateCaseDTO);
                compensateCaseDTO.setCompensateId(compensateDTO.getId());
                compensateCaseDTO.setResult(failMsg);
                compensateCaseDTO.setStatus(CompensateStatusEnum.FAILED.getStatus());
                compensateCaseService.save(compensateCaseDTO);

                //增加hist记录
                CompensateHistDTO compensateHistDTO = new CompensateHistDTO();
                BeanUtils.copyProperties(compensateDTO, compensateHistDTO);
                compensateHistDTO.setCompensateId(compensateDTO.getId());
                compensateHistDTO.setStatus(CompensateStatusEnum.FAILED.getStatus());
                compensateHistService.save(compensateHistDTO);
            }

            return Result.ok(true);
        } catch (CompensateException e) {
            LOGGER.error("compensateFailedCallBack err, SntException: {}", e);
            return Result.fail(e);
        } catch (Exception e) {
            LOGGER.error("compensateFailedCallBack err, e: {}", e);
            return Result.fail(new UnknownException(e));
        }
    }

    @Override
    public Result<Boolean> save(CompensateDTO compensateDTO) {
        try {
            return Result.ok(compensateService.save(compensateDTO) == 1);
        } catch (CompensateException e) {
            LOGGER.error("save err, SntException: {}", e);
            return Result.fail(e);
        } catch (Exception e) {
            LOGGER.error("save err, e: {}", e);
            return Result.fail(new UnknownException(e));
        }
    }

    @Override
    public Result<List<CompensateDTO>> query(CompensateQuery query) {
        try {
            return Result.ok(compensateService.query(query));
        } catch (CompensateException e) {
            LOGGER.error("query err, SntException: {}", e);
            return Result.fail(e);
        } catch (Exception e) {
            LOGGER.error("query err, e: {}", e);
            return Result.fail(new UnknownException(e));
        }
    }
}
