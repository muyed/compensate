package com.muye.compensate.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;
import javax.annotation.Resource;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

import com.muye.compensate.DO.CompensateDO;
import com.muye.compensate.DTO.CompensateDTO;
import com.muye.compensate.query.CompensateQuery;
import com.muye.compensate.manager.CompensateManager;
import com.muye.compensate.service.CompensateService;


/**
 *
 * <pre>
 * Author muye	2018年07月31日 Created
 * </pre>
 */
@Service
public class CompensateServiceImpl implements CompensateService {

    @Resource
    private CompensateManager compensateManager;

    public Integer save(CompensateDTO compensateDTO) {
        //对象转换
        CompensateDO compensateDO = new CompensateDO();
        BeanUtils.copyProperties(compensateDTO,compensateDO);

        Integer cnt = compensateManager.save(compensateDO);

        return cnt;
    }

    public Integer update(CompensateDTO compensateDTO) {
        CompensateDO compensateDO;
        if((compensateDO = compensateManager.getById(compensateDTO.getId())) == null)
            return 0;

        //复制要修改对象的属性
        BeanUtils.copyProperties(compensateDTO, compensateDO);

        return compensateManager.update(compensateDO);
    }

    public Integer delete(Long id) {
        Integer cnt = compensateManager.delete(id);
        return cnt;
    }

    public CompensateDTO getById(Long id) {

        CompensateDO compensateDO = compensateManager.getById(id);
        //对象转换
        CompensateDTO compensateDTO = null;
        if(null != compensateDO){
            compensateDTO = new CompensateDTO();
            BeanUtils.copyProperties(compensateDO,compensateDTO);
        }


        return compensateDTO;
    }

    public List<CompensateDTO> query(CompensateQuery compensateQuery) {
        List<CompensateDO> list = compensateManager.query(compensateQuery);
        if(CollectionUtils.isEmpty(list)){
            return new ArrayList();
        }

        if(null == compensateQuery.getTotalRecored()){
            Integer cnt = compensateManager.queryCount(compensateQuery);
            compensateQuery.setTotalRecored(cnt == null ? 0 : cnt);
        }

        List<CompensateDTO> resultList = list.stream()
                .map(d -> {
                    CompensateDTO dto = new CompensateDTO();
                    BeanUtils.copyProperties(d, dto);
                    return dto;
                })
                .collect(Collectors.toList());

        return resultList;
    }

}
