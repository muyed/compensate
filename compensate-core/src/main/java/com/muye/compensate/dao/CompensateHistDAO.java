package com.muye.compensate.dao;

import org.apache.ibatis.annotations.Param;

import com.muye.compensate.DO.CompensateHistDO;
import com.muye.compensate.query.CompensateHistQuery;


/**
 *
 * <pre>
 * Author muye	2018年07月31日 Created
 * </pre>
 */
public interface CompensateHistDAO extends SntBaseDAO<CompensateHistDO, CompensateHistQuery> {

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
    CompensateHistDO queryById(@Param("id") Long id);
}