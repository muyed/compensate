package com.muye.compensate.manager;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import javax.annotation.Resource;
import java.util.List;

import com.muye.compensate.DO.CompensateHistDO;
import com.muye.compensate.query.CompensateHistQuery;
import com.muye.compensate.dao.CompensateHistDAO;

/**
 *
 * <pre>
 * Author muye	2018年07月31日 Created
 * </pre>
 */
@Component
public class CompensateHistManager {

    @Resource
    private CompensateHistDAO compensateHistDAO;

    public Integer save(CompensateHistDO compensateHistDO) {
        Integer cnt = compensateHistDAO.insert(compensateHistDO);
        return cnt;
    }

    public Integer delete(Long id) {
        Assert.notNull(id, "id cant null");
        Integer cnt = compensateHistDAO.delete(id);
        return cnt;
    }

    public Integer update(CompensateHistDO compensateHistDO) {
        Integer cnt = compensateHistDAO.update(compensateHistDO);
        return cnt;
    }

    public CompensateHistDO getById(Long id) {
        CompensateHistDO compensateHistDO = compensateHistDAO.queryById(id);
        return compensateHistDO;
    }

    public List<CompensateHistDO> query(CompensateHistQuery compensateHistQuery) {
        List<CompensateHistDO> compensateHistList = compensateHistDAO.query(compensateHistQuery);
        return compensateHistList;
    }

    public Integer queryCount(CompensateHistQuery compensateHistQuery) {
        Integer cnt = compensateHistDAO.queryCount(compensateHistQuery);
        return cnt;
    }
}
