package com.muye.compensate.dao;

import org.apache.ibatis.annotations.Param;

import com.muye.compensate.DO.CompensateCaseDO;
import com.muye.compensate.query.CompensateCaseQuery;


/**
 *
 * <pre>
 * Author muye	2018年07月31日 Created
 * </pre>
 */
public interface CompensateCaseDAO extends SntBaseDAO<CompensateCaseDO, CompensateCaseQuery> {

    /**
     * 逻辑删除
     * @param id
     * @return
     */
    Integer delete(@Param("id") Long id);

   /**
     * 根据主键查询
     * @param id
     * @return
     */
    CompensateCaseDO queryById(@Param("id") Long id);
}