package com.muye.compensate.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;
import javax.annotation.Resource;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

import com.muye.compensate.DO.CompensateCaseDO;
import com.muye.compensate.DTO.CompensateCaseDTO;
import com.muye.compensate.query.CompensateCaseQuery;
import com.muye.compensate.manager.CompensateCaseManager;
import com.muye.compensate.service.CompensateCaseService;


/**
 *
 * <pre>
 * Author muye	2018年07月31日 Created
 * </pre>
 */
@Service
public class CompensateCaseServiceImpl implements CompensateCaseService {

    @Resource
    private CompensateCaseManager compensateCaseManager;

    public Integer save(CompensateCaseDTO compensateCaseDTO) {
        //对象转换
        CompensateCaseDO compensateCaseDO = new CompensateCaseDO();
        BeanUtils.copyProperties(compensateCaseDTO,compensateCaseDO);

        Integer cnt = compensateCaseManager.save(compensateCaseDO);

        return cnt;
    }

    public Integer update(CompensateCaseDTO compensateCaseDTO) {
        CompensateCaseDO compensateCaseDO;
        if((compensateCaseDO = compensateCaseManager.getById(compensateCaseDTO.getId())) == null)
            return 0;

        //复制要修改对象的属性
        BeanUtils.copyProperties(compensateCaseDTO, compensateCaseDO);

        return compensateCaseManager.update(compensateCaseDO);
    }

    public Integer delete(Long id) {
        Integer cnt = compensateCaseManager.delete(id);
        return cnt;
    }

    public CompensateCaseDTO getById(Long id) {

        CompensateCaseDO compensateCaseDO = compensateCaseManager.getById(id);
        //对象转换
        CompensateCaseDTO compensateCaseDTO = null;
        if(null != compensateCaseDO){
            compensateCaseDTO = new CompensateCaseDTO();
            BeanUtils.copyProperties(compensateCaseDO,compensateCaseDTO);
        }


        return compensateCaseDTO;
    }

    public List<CompensateCaseDTO> query(CompensateCaseQuery compensateCaseQuery) {
        List<CompensateCaseDO> list = compensateCaseManager.query(compensateCaseQuery);
        if(CollectionUtils.isEmpty(list)){
            return new ArrayList();
        }

        if(null == compensateCaseQuery.getTotalRecored()){
            Integer cnt = compensateCaseManager.queryCount(compensateCaseQuery);
            compensateCaseQuery.setTotalRecored(cnt == null ? 0 : cnt);
        }

        List<CompensateCaseDTO> resultList = list.stream()
                .map(d -> {
                    CompensateCaseDTO dto = new CompensateCaseDTO();
                    BeanUtils.copyProperties(d, dto);
                    return dto;
                })
                .collect(Collectors.toList());

        return resultList;
    }

}
