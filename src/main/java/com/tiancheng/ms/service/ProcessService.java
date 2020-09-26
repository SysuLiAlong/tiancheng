package com.tiancheng.ms.service;

import com.tiancheng.ms.common.context.ContextHolder;
import com.tiancheng.ms.common.exception.BusinessException;
import com.tiancheng.ms.constant.ErrorCode;
import com.tiancheng.ms.constant.ProduceProcessConstant;
import com.tiancheng.ms.dao.mapper.ProcessMapper;
import com.tiancheng.ms.dao.mapper.ProduceProcessMapper;
import com.tiancheng.ms.dto.ProcesssDTO;
import com.tiancheng.ms.dto.ProduceProcessDTO;
import com.tiancheng.ms.dto.param.ProcessParam;
import com.tiancheng.ms.entity.ProcessEntity;
import com.tiancheng.ms.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ProcessService {

    @Autowired
    private ProcessMapper processMapper;

    @Autowired
    private ProduceProcessMapper produceProcessMapper;

    @Autowired
    private ProduceProcessConstant produceProcessConstant;

    public synchronized void addProcess(ProcessParam param) {
        boolean isNameExist = processMapper.selectOneByName(param.getName()) == null ? false : true;
        if (isNameExist) {
            throw new BusinessException(ErrorCode.FAIL,"流程名称重复！");
        }
        Integer maxPriority = processMapper.selectMaxPriority();
        ProcessEntity entity = new ProcessEntity();
        entity.setUpdateBy(ContextHolder.getUser().getUserName());
        entity.setCreateBy(ContextHolder.getUser().getUserName());
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        entity.setPriority(maxPriority+1);
        processMapper.insertSelective(BeanUtils.copy(param, entity));
    }

    public void updateProcess(ProcessParam param) {
        ProcessEntity entity = processMapper.selectOneByName(param.getName());
        boolean isNameExist = entity != null && entity.getId() != param.getId();
        if (isNameExist) {
            throw new BusinessException(ErrorCode.FAIL,"流程名称重复！");
        }
        entity = processMapper.selectByPrimaryKey(param.getId());
        if(entity == null) {
            throw new BusinessException(ErrorCode.FAIL,"修改流程不存在");
        }
        BeanUtils.copy(param,entity);
        entity.setUpdateTime(new Date());
        entity.setUpdateBy(ContextHolder.getUser().getUserName());
        processMapper.updateByPrimaryKey(entity);
    }

    public List<ProcesssDTO> listProcess() {
        return BeanUtils.copy(processMapper.listPriorityProcess(), ProcesssDTO.class);
    }

    public void deleteProcess(Integer processId) {
        ProcessEntity entity = processMapper.selectByPrimaryKey(processId);
        if(entity == null) {
            throw new BusinessException(ErrorCode.FAIL,"删除内容不存在");
        }
        processMapper.deleteByPrimaryKey(processId);
    }

    public void exchangePriority(Integer first, Integer second) {

        List<ProduceProcessDTO> firstProduceProcess = produceProcessMapper.selectUnOverProduceContainProcessId(first,produceProcessConstant.getEndId());
        if (firstProduceProcess.size() > 0) {
            StringBuilder produceIds = new StringBuilder();
            firstProduceProcess.stream().forEach(item -> {
                produceIds.append(item.getProduceId());
                produceIds.append(",");
            });
            String producemsg = produceIds.toString().substring(1,produceIds.length() - 1);
            ProcessEntity processEntity = processMapper.selectByPrimaryKey(first);
            throw new BusinessException(ErrorCode.FAIL,"流程" + processEntity.getName() + "已经在生产计划" + producemsg + "中使用，请等待生产计划关闭后再调整优先级！");
        }
        List<ProduceProcessDTO> secondProduceProcess = produceProcessMapper.selectUnOverProduceContainProcessId(second,produceProcessConstant.getEndId());
        if (secondProduceProcess.size() > 0) {
            StringBuilder produceIds = new StringBuilder();
            secondProduceProcess.stream().forEach(item -> {
                produceIds.append(item.getProduceId());
                produceIds.append(",");
            });
            String producemsg = produceIds.toString().substring(0,produceIds.length() - 1);
            ProcessEntity processEntity = processMapper.selectByPrimaryKey(second);
            throw new BusinessException(ErrorCode.FAIL,"流程" + processEntity.getName() + "已经在生产计划" + producemsg + "中使用，请等待生产计划关闭后再调整优先级！");
        }

        ProcessEntity firstProcess = processMapper.selectByPrimaryKey(first);
        ProcessEntity secondProcess = processMapper.selectByPrimaryKey(second);
        Integer tempPriority = firstProcess.getPriority();
        firstProcess.setPriority(secondProcess.getPriority());
        secondProcess.setPriority(tempPriority);
        firstProcess.setUpdateTime(new Date());
        secondProcess.setUpdateTime(new Date());
        firstProcess.setUpdateBy(ContextHolder.getUser().getUserName());
        secondProcess.setUpdateBy(ContextHolder.getUser().getUserName());
        processMapper.updateByPrimaryKey(firstProcess);
        processMapper.updateByPrimaryKey(secondProcess);
    }

    public ProcesssDTO processDetail(Integer processId) {
        return BeanUtils.copy(processMapper.selectByPrimaryKey(processId), ProcesssDTO.class);
    }
}
