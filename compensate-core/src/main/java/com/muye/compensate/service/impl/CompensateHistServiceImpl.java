package com.muye.compensate.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;
import javax.annotation.Resource;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

import com.muye.compensate.DO.CompensateHistDO;
import com.muye.compensate.DTO.CompensateHistDTO;
import com.muye.compensate.query.CompensateHistQuery;
import com.muye.compensate.manager.CompensateHistManager;
import com.muye.compensate.service.CompensateHistService;


/**
 *
 * <pre>
 * Author muye	2018年07月31日 Created
 * </pre>
 */
@Service
public class CompensateHistServiceImpl implements CompensateHistService {

    @Resource
    private CompensateHistManager compensateHistManager;

    public Integer save(CompensateHistDTO compensateHistDTO) {
        //对象转换
        CompensateHistDO compensateHistDO = new CompensateHistDO();
        BeanUtils.copyProperties(compensateHistDTO,compensateHistDO);

        Integer cnt = compensateHistManager.save(compensateHistDO);

        return cnt;
    }

    public Integer update(CompensateHistDTO compensateHistDTO) {
        CompensateHistDO compensateHistDO;
        if((compensateHistDO = compensateHistManager.getById(compensateHistDTO.getId())) == null)
            return 0;

        //复制要修改对象的属性
        BeanUtils.copyProperties(compensateHistDTO, compensateHistDO);

        return compensateHistManager.update(compensateHistDO);
    }

    public Integer delete(Long id) {
        Integer cnt = compensateHistManager.delete(id);
        return cnt;
    }

    public CompensateHistDTO getById(Long id) {

        CompensateHistDO compensateHistDO = compensateHistManager.getById(id);
        //对象转换
        CompensateHistDTO compensateHistDTO = null;
        if(null != compensateHistDO){
            compensateHistDTO = new CompensateHistDTO();
            BeanUtils.copyProperties(compensateHistDO,compensateHistDTO);
        }


        return compensateHistDTO;
    }

    public List<CompensateHistDTO> query(CompensateHistQuery compensateHistQuery) {
        List<CompensateHistDO> list = compensateHistManager.query(compensateHistQuery);
        if(CollectionUtils.isEmpty(list)){
            return new ArrayList();
        }

        if(null == compensateHistQuery.getTotalRecored()){
            Integer cnt = compensateHistManager.queryCount(compensateHistQuery);
            compensateHistQuery.setTotalRecored(cnt == null ? 0 : cnt);
        }

        List<CompensateHistDTO> resultList = list.stream()
                .map(d -> {
                    CompensateHistDTO dto = new CompensateHistDTO();
                    BeanUtils.copyProperties(d, dto);
                    return dto;
                })
                .collect(Collectors.toList());

        return resultList;
    }

}
