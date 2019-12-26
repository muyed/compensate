package com.muye.compensate.service;

import java.util.List;

import com.muye.compensate.DTO.CompensateDTO;
import com.muye.compensate.query.CompensateQuery;


/**
 *
 * <pre>
 * Author muye	2018年07月31日 Created
 * </pre>
 */
public interface CompensateService {

    /**
     * 保存数据
     * @param compensateDTO
     * @return
     */
    Integer save(CompensateDTO compensateDTO);

    /**
     * 根据ID逻辑删除数据
     * @param id
     * @return
     */
    Integer delete(Long id);

    /**
     * 修改数据,必须带有ID
     * @param compensateDTO
     * @return
     */
    Integer update(CompensateDTO compensateDTO);

    /**
     * 根据ID查询数据
     * @param id
     * @return
     */
    CompensateDTO getById(Long id);

    /**
     * 根据查询条件获取数据
     * @param compensateQuery
     * @return
     */
    List<CompensateDTO> query(CompensateQuery compensateQuery);

}
