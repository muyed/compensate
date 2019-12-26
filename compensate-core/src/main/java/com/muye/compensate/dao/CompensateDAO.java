package com.muye.compensate.dao;

import org.apache.ibatis.annotations.Param;

import com.muye.compensate.DO.CompensateDO;
import com.muye.compensate.query.CompensateQuery;


/**
 *
 * <pre>
 * Author muye	2018年07月31日 Created
 * </pre>
 */
public interface CompensateDAO extends SntBaseDAO<CompensateDO, CompensateQuery> {

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
    CompensateDO queryById(@Param("id") Long id);
}