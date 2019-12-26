package com.muye.compensate.service;

import java.util.List;

import com.muye.compensate.DTO.CompensateHistDTO;
import com.muye.compensate.query.CompensateHistQuery;


/**
 *
 * <pre>
 * Author muye	2018年07月31日 Created
 * </pre>
 */
public interface CompensateHistService {

    /**
     * 保存数据
     * @param compensateHistDTO
     * @return
     */
    Integer save(CompensateHistDTO compensateHistDTO);

    /**
     * 根据ID逻辑删除数据
     * @param id
     * @return
     */
    Integer delete(Long id);

    /**
     * 修改数据,必须带有ID
     * @param compensateHistDTO
     * @return
     */
    Integer update(CompensateHistDTO compensateHistDTO);

    /**
     * 根据ID查询数据
     * @param id
     * @return
     */
    CompensateHistDTO getById(Long id);

    /**
     * 根据查询条件获取数据
     * @param compensateHistQuery
     * @return
     */
    List<CompensateHistDTO> query(CompensateHistQuery compensateHistQuery);

}
