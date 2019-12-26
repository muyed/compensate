package com.muye.compensate.dao;

import java.util.List;

/**
 * 基础服务
 * @param <D> do
 * @param <Q> query
 * @author qinshuangping
 */
public interface SntBaseDAO<D,Q> {

    /**
     * 添加
     * @param d
     * @return
     */
    Integer insert(D d);

    /**
     * 修改
     * @param d
     * @return
     */
    Integer update(D d);

    /**
     * 删除
     * @param id
     * @return
     */
    Integer delete(Long id);

    /**
     * 查询
     * @param q
     * @return
     */
    List<D> query(Q q);

    /**
     * 查询记录数
     * @param q
     * @return
     */
    Integer queryCount(Q q);
}
