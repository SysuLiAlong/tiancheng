package com.tiancheng.ms.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tiancheng.ms.common.context.ContextHolder;
import com.tiancheng.ms.common.dto.Page;
import com.tiancheng.ms.dao.mapper.AlarmMapper;
import com.tiancheng.ms.dto.AlarmDTO;
import com.tiancheng.ms.dto.param.AlarmParam;
import com.tiancheng.ms.dto.param.AlarmQueryParam;
import com.tiancheng.ms.entity.AlarmEntity;
import com.tiancheng.ms.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AlarmService {

    @Autowired
    private AlarmMapper alarmMapper;

    public void addAlarm(AlarmParam param) {
        AlarmEntity entity = new AlarmEntity();
        BeanUtils.copy(param, entity);
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        alarmMapper.insert(entity);
    }


    public Page<AlarmDTO> pageQry(AlarmQueryParam queryParam) {
        PageHelper.startPage(queryParam.getPageNo(), queryParam.getPageSize());
        String currentUser = ContextHolder.getUser().getUserName();
        List<AlarmDTO> alarmDTOList = alarmMapper.listAlarm(currentUser, queryParam.getType());
        for (AlarmDTO alarmDTO : alarmDTOList) {
            if (AlarmParam.OVER_TIME_ALARM.equals(alarmDTO.getType())) {
                alarmDTO.setContent(AlarmDTO.OVER_TIME_CONTENT_TMPL
                        .replace("{produceCode}", alarmDTO.getProduceCode())
                        .replace("{productName}", alarmDTO.getProductName())
                        .replace("{processName}", alarmDTO.getProcessName()));
            } else {
                alarmDTO.setContent(AlarmDTO.FAULT_RATE_CONTENT_TMPL
                        .replace("{produceCode}", alarmDTO.getProduceCode())
                        .replace("{productName}", alarmDTO.getProductName()));
            }
        }
        PageInfo pageInfo = new PageInfo(alarmDTOList);
        return new Page<>(pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal(), alarmDTOList);
    }
}
