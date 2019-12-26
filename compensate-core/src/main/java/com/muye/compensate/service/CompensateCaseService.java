package com.muye.compensate.service;

import java.util.List;

import com.muye.compensate.DTO.CompensateCaseDTO;
import com.muye.compensate.query.CompensateCaseQuery;


/**
 *
 * <pre>
 * Author muye	2018年07月31日 Created
 * </pre>
 */
public interface CompensateCaseService {

    /**
     * 保存数据
     * @param compensateCaseDTO
     * @return
     */
    Integer save(CompensateCaseDTO compensateCaseDTO);

    /**
     * 根据ID逻辑删除数据
     * @param id
     * @return
     */
    Integer delete(Long id);

    /**
     * 修改数据,必须带有ID
     * @param compensateCaseDTO
     * @return
     */
    Integer update(CompensateCaseDTO compensateCaseDTO);

    /**
     * 根据ID查询数据
     * @param id
     * @return
     */
    CompensateCaseDTO getById(Long id);

    /**
     * 根据查询条件获取数据
     * @param compensateCaseQuery
     * @return
     */
    List<CompensateCaseDTO> query(CompensateCaseQuery compensateCaseQuery);

}
