package com.muye.compensate.manager;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import javax.annotation.Resource;
import java.util.List;

import com.muye.compensate.DO.CompensateCaseDO;
import com.muye.compensate.query.CompensateCaseQuery;
import com.muye.compensate.dao.CompensateCaseDAO;

/**
 *
 * <pre>
 * Author muye	2018年07月31日 Created
 * </pre>
 */
@Component
public class CompensateCaseManager {

    @Resource
    private CompensateCaseDAO compensateCaseDAO;

    public Integer save(CompensateCaseDO compensateCaseDO) {
        Integer cnt = compensateCaseDAO.insert(compensateCaseDO);
        return cnt;
    }

    public Integer delete(Long id) {
        Assert.notNull(id, "id cant null");
        Integer cnt = compensateCaseDAO.delete(id);
        return cnt;
    }

    public Integer update(CompensateCaseDO compensateCaseDO) {
        Integer cnt = compensateCaseDAO.update(compensateCaseDO);
        return cnt;
    }

    public CompensateCaseDO getById(Long id) {
        CompensateCaseDO compensateCaseDO = compensateCaseDAO.queryById(id);
        return compensateCaseDO;
    }

    public List<CompensateCaseDO> query(CompensateCaseQuery compensateCaseQuery) {
        List<CompensateCaseDO> compensateCaseList = compensateCaseDAO.query(compensateCaseQuery);
        return compensateCaseList;
    }

    public Integer queryCount(CompensateCaseQuery compensateCaseQuery) {
        Integer cnt = compensateCaseDAO.queryCount(compensateCaseQuery);
        return cnt;
    }
}
