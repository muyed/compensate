package com.muye.compensate.manager;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import javax.annotation.Resource;
import java.util.List;

import com.muye.compensate.DO.CompensateDO;
import com.muye.compensate.query.CompensateQuery;
import com.muye.compensate.dao.CompensateDAO;

/**
 *
 * <pre>
 * Author muye	2018年07月31日 Created
 * </pre>
 */
@Component
public class CompensateManager {

    @Resource
    private CompensateDAO compensateDAO;

    public Integer save(CompensateDO compensateDO) {
        Integer cnt = compensateDAO.insert(compensateDO);
        return cnt;
    }

    public Integer delete(Long id) {
        Assert.notNull(id, "id cant null");
        Integer cnt = compensateDAO.delete(id);
        return cnt;
    }

    public Integer update(CompensateDO compensateDO) {
        Integer cnt = compensateDAO.update(compensateDO);
        return cnt;
    }

    public CompensateDO getById(Long id) {
        CompensateDO compensateDO = compensateDAO.queryById(id);
        return compensateDO;
    }

    public List<CompensateDO> query(CompensateQuery compensateQuery) {
        List<CompensateDO> compensateList = compensateDAO.query(compensateQuery);
        return compensateList;
    }

    public Integer queryCount(CompensateQuery compensateQuery) {
        Integer cnt = compensateDAO.queryCount(compensateQuery);
        return cnt;
    }
}
