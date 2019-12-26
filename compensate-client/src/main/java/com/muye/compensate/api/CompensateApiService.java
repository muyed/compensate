package com.muye.compensate.api;

import com.muye.compensate.DTO.CompensateDTO;
import com.muye.compensate.query.CompensateQuery;
import com.muye.compensate.result.Result;

import java.util.List;

/**
 * created by dahan at 2018/8/1
 */
public interface CompensateApiService {

    /**
     * 尝试补偿 返回true才可以执行补偿
     * @param compensateId
     * @return
     */
    Result<Boolean> tryCompensate(Long compensateId);

    /**
     * 补偿成功回调
     * @param compensateId
     * @return
     */
    Result<Boolean> compensateSuccessfulCallBack(Long compensateId);

    /**
     * 补偿失败回调
     * @param compensateId
     * @return
     */
    Result<Boolean> compensateFailedCallBack(Long compensateId, String failMsg);

    /**
     * 添加补偿任务
     * @param compensateDTO
     * @return
     */
    Result<Boolean> save(CompensateDTO compensateDTO);

    /**
     *
     * @param query
     * @return
     */
    Result<List<CompensateDTO>> query(CompensateQuery query);
}
