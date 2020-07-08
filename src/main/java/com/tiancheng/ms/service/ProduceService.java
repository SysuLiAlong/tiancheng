package com.tiancheng.ms.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tiancheng.ms.common.context.ContextHolder;
import com.tiancheng.ms.common.dto.Page;
import com.tiancheng.ms.common.exception.BusinessException;
import com.tiancheng.ms.constant.*;
import com.tiancheng.ms.dao.mapper.*;
import com.tiancheng.ms.dto.ProduceDetailDTO;
import com.tiancheng.ms.dto.ProduceProcessDTO;
import com.tiancheng.ms.dto.param.ProduceParam;
import com.tiancheng.ms.dto.param.ProduceProcessAndMsgParam;
import com.tiancheng.ms.dto.param.ProduceQueryParam;
import com.tiancheng.ms.entity.ProcessEntity;
import com.tiancheng.ms.entity.ProduceEntity;
import com.tiancheng.ms.entity.ProduceMsgEntity;
import com.tiancheng.ms.entity.ProduceProcessEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ProduceService {

    @Autowired
    private ProduceMapper produceMapper;

    @Autowired
    private ProduceMsgMapper produceMsgMapper;

    @Autowired
    private ProduceProcessMapper produceProcessMapper;

    @Autowired
    private ProcessMapper processMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleConstant userRoleConstant;

    @Autowired
    private ProduceProcessConstant produceProcessConstant;

    public Page<ProduceDetailDTO> pageQryProduce(ProduceQueryParam queryParam) {
        PageHelper.startPage(queryParam.getPageNo(),queryParam.getPageSize());
        String chargeUserName = null;
        if (ContextHolder.getUser().getRole().equals(userRoleConstant.getCommonUser())) {
            chargeUserName = queryParam.getChargeUserName();
        }
        List<ProduceDetailDTO> produceDetailDTOS =  produceMapper.pageQryProduceDetail(queryParam.getOrderCode(),
                queryParam.getOrderParam(),chargeUserName);
        PageInfo pageInfo = new PageInfo(produceDetailDTOS);
        return new Page<>(pageInfo.getPageNum(),pageInfo.getPageSize(),pageInfo.getTotal(),produceDetailDTOS);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addProduce(ProduceParam produceParam) {
        ProduceEntity entity = new ProduceEntity();
        BeanUtils.copyProperties(produceParam,entity);
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        entity.setCreateBy(ContextHolder.getUser().getUserName());
        entity.setUpdateBy(ContextHolder.getUser().getUserName());
        entity.setCode(generiateProduceCode());
        produceMapper.insert(entity);

        List<ProcessEntity> processEntities = processMapper.getProduceProcess(entity.getProductCode());
        if (processEntities.size() != 0) {
            List<ProduceProcessEntity> produceProcessEntities = new ArrayList<>(processEntities.size());
            Integer nextProcess = produceProcessConstant.getEndId();
            //添加“结束”流程
            ProduceProcessEntity endProcess = new ProduceProcessEntity();
            endProcess.setProduceId(entity.getId());
            endProcess.setProcessId(produceProcessConstant.getEndId());
            endProcess.setProcessName(produceProcessConstant.getEndName());
            endProcess.setChargeUserName(produceProcessConstant.getUserName());
            produceProcessEntities.add(endProcess);
            for (int i= 0;i < processEntities.size(); i++) {
                ProcessEntity processEntity = processEntities.get(i);
                ProduceProcessEntity produceProcessEntity = new ProduceProcessEntity();
                produceProcessEntity.setProcessId(processEntity.getId());
                produceProcessEntity.setProduceId(entity.getId());
                produceProcessEntity.setProcessName(processEntity.getName());
                produceProcessEntity.setChargeUserName(processEntity.getChargeUserName());
                produceProcessEntity.setNextProcess(nextProcess);
                nextProcess = processEntity.getId();
                if (i == processEntities.size() - 1) {
                    produceProcessEntity.setStatus(ProduceProcessStatusConstant.ARRIVE_STATUS);
                } else {
                    produceProcessEntity.setStatus(ProduceProcessStatusConstant.INIT_STATUS);
                }
                produceProcessEntities.add(produceProcessEntity);
            }
            //添加“开始”流程
            ProduceProcessEntity startProcess = new ProduceProcessEntity();
            startProcess.setProcessId(produceProcessConstant.getStartId());
            startProcess.setProduceId(entity.getId());
            startProcess.setStatus(ProduceProcessStatusConstant.END_STATUS);
            startProcess.setProcessName(produceProcessConstant.getStartName());
            startProcess.setChargeUserName(produceProcessConstant.getUserName());
            startProcess.setNextProcess(nextProcess);
            produceProcessEntities.add(startProcess);
            produceProcessMapper.insertList(produceProcessEntities);
        }

        if (!StringUtils.isEmpty(produceParam.getContent())) {
            ProduceMsgEntity produceMsgEntity = new ProduceMsgEntity();
            produceMsgEntity.setProduceId(entity.getId());
            produceMsgEntity.setContent(produceParam.getContent());
            produceMsgEntity.setCreateTime(new Date());
            produceMsgEntity.setCreateBy(ContextHolder.getUser().getUserName());
            produceMsgEntity.setType(1);
            produceMsgMapper.insert(produceMsgEntity);
        }
    }

    public void addProduceMsg(ProduceMsgEntity entity) {

        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        entity.setCreateBy(ContextHolder.getUser().getUserName());
        entity.setUpdateBy(ContextHolder.getUser().getUserName());
        produceMsgMapper.insert(entity);
    }

    public List<ProduceMsgEntity> listProduceMsg(Integer produceId) {
        List<ProduceMsgEntity> entities =  produceMsgMapper.listProduceMsg(produceId);
        entities.stream().forEach(entity -> {
            String processName = StringUtils.isEmpty(entity.getProcessName()) ? "" : entity.getProcessName() ;
            String createBy = StringUtils.isEmpty(entity.getCreateBy()) ? "" : entity.getCreateBy();
            String content = StringUtils.isEmpty(entity.getContent()) ? "" : entity.getContent();
            String amount = entity.getAmount() == null ? "" : String.valueOf(entity.getAmount());
            switch (entity.getType()){
                case 1: {
                    entity.setContent(MessageConstant.CUSTOM_MESSAGE
                            .replace("{username}",createBy)
                            .replace("{process}",processName)
                            .replace("{content}",content)
                    );
                    break;
                }
                case 2: {
                    entity.setContent(MessageConstant.ACCEPT_MESSAGE
                            .replace("{username}",createBy)
                            .replace("{process}",processName)
                            .replace("{amount}",amount)
                    );
                    break;
                }
                case 4: {
                    entity.setContent(MessageConstant.TRANSMIT_MESSAGE
                            .replace("{username}",createBy)
                            .replace("{process}",processName)
                            .replace("{amount}",amount)
                    );
                    break;
                }
                case 3: {
                    entity.setContent(MessageConstant.REJECT_MESSAGE
                            .replace("{username}",createBy)
                            .replace("{process}",processName)
                    );
                }
                default: entity.setContent("消息异常");
            }
        });
        return entities;
    }

    public List<ProduceProcessDTO> listProduceProcess(Integer produceId) {
        return produceProcessMapper.listProduceProcess(produceId);
    }

    public ProduceDetailDTO getProduceDetail(Integer produceId) {
        ProduceEntity produceEntity = produceMapper.selectByPrimaryKey(produceId);
        ProcessEntity currentProcess = produceProcessMapper.selectCurrentProcess(produceId);
        ProduceDetailDTO produceDetailDTO = new ProduceDetailDTO();
        BeanUtils.copyProperties(produceEntity,produceDetailDTO);
        if (currentProcess != null) {
            produceDetailDTO.setProcessChargeUserName(currentProcess.getChargeUserName());
            produceDetailDTO.setProduceProcessName(currentProcess.getName());
        }
        return produceDetailDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    public void acceptProcess(ProduceProcessAndMsgParam param) {
        ProduceProcessEntity produceProcessEntity = param.getProduceProcessEntity();
        produceProcessEntity.setStatus(ProduceProcessStatusConstant.ACCEPT_STATUS);
        produceProcessMapper.updateByPrimaryKey(produceProcessEntity);

        ProduceProcessEntity nextProcess = produceProcessMapper.getNextProcess(produceProcessEntity.getId());
        nextProcess.setStatus(ProduceProcessStatusConstant.START_STATUS);
        nextProcess.setStartTime(new Date());
        nextProcess.setStartNum(produceProcessEntity.getEndNum());
        produceProcessMapper.updateByPrimaryKey(nextProcess);

        addProduceMsg(param.getProduceMsgEntity());
    }

    @Transactional(rollbackFor = Exception.class)
    public void rejectProcess(ProduceProcessAndMsgParam param) {
        ProduceProcessEntity produceProcessEntity = param.getProduceProcessEntity();
        produceProcessEntity.setStatus(ProduceProcessStatusConstant.START_STATUS);
        produceProcessEntity.setEndTime(null);
        produceProcessMapper.updateByPrimaryKey(produceProcessEntity);

        ProduceProcessEntity nextProcess = produceProcessMapper.getNextProcess(produceProcessEntity.getId());
        nextProcess.setStatus(ProduceProcessStatusConstant.INIT_STATUS);
        produceProcessMapper.updateByPrimaryKey(nextProcess);

        addProduceMsg(param.getProduceMsgEntity());
    }

    public ProduceProcessEntity getLastProcess(Integer produceId) {
        ProduceProcessEntity currentProcess = getCurrentProcess(produceId);
        if (currentProcess == null) {
            throw new BusinessException(ErrorCode.FAIL,"数据异常，无法查到当前流程,请重试！");
        }
        return produceProcessMapper.getLastProcess(produceId,currentProcess.getProcessId());
    }

    public ProduceProcessEntity getCurrentProcess(Integer produceId) {
        ProduceProcessEntity currentProcess =  produceProcessMapper.getCurrentProcess(produceId);
        if (currentProcess == null) {
            throw new BusinessException(ErrorCode.FAIL,"数据异常，无法查到当前流程,请重试！");
        }
        return currentProcess;
    }

    public ProduceProcessEntity getNextProcess(Integer produceId) {
        ProduceProcessEntity currentProcess = getCurrentProcess(produceId);
        if (currentProcess == null) {
            throw new BusinessException(ErrorCode.FAIL,"数据异常，无法查到当前流程,请重试！");
        }
        return produceProcessMapper.getNextProcess(currentProcess.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void transmitProcess(ProduceProcessAndMsgParam param) {
        ProduceProcessEntity produceProcessEntity = param.getProduceProcessEntity();
        produceProcessEntity.setStatus(ProduceProcessStatusConstant.END_STATUS);
        produceProcessEntity.setEndTime(new Date());
        produceProcessMapper.updateByPrimaryKey(produceProcessEntity);

        ProduceProcessEntity nextProcess = produceProcessMapper.getNextProcess(produceProcessEntity.getId());
        nextProcess.setStatus(ProduceProcessStatusConstant.ARRIVE_STATUS);
        produceProcessMapper.updateByPrimaryKey(nextProcess);

        addProduceMsg(param.getProduceMsgEntity());
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteProduce(Integer produceId) {
        produceMapper.deleteByPrimaryKey(produceId);
        produceProcessMapper.deleteByProduceId(produceId);
        produceMsgMapper.deleteByProduceId(produceId);
    }

    private String generiateProduceCode() {
        String lastCode = produceMapper.getLastProduce();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR)%100;
        int month = calendar.get(Calendar.MONTH) + 1;
        //20050001 = 20年 * 1000000 + 5月 * 10000 + 第一个生产计划
        boolean isEqualYearMonth = !StringUtils.isEmpty(lastCode) && Integer.valueOf(lastCode.substring(0,2)).equals(year)
                && Integer.valueOf(lastCode.substring(2,4)).equals(month);
        int num;
        if (isEqualYearMonth) {
            num = Integer.valueOf(lastCode.substring(4)) + 1;
        } else {
            num = 1;
        }
        return String.format("%02d%02d%04d",year,month,num);
    }

    public static void main(String[] args) {
        String lastCode = new String("20060002");
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR)%100;
        int month = calendar.get(Calendar.MONTH) + 1;
        //20050001 = 20年 * 1000000 + 5月 * 10000 + 第一个生产计划
        boolean isEqualYearMonth = Integer.valueOf(lastCode.substring(0,2)).equals(year)
                && Integer.valueOf(lastCode.substring(2,4)).equals(month);
        int num;
        if (isEqualYearMonth) {
            num = Integer.valueOf(lastCode.substring(4)) + 1;
        } else {
            num = 1;
        }
        System.out.println(String.format("%02d%02d%04d",year,month,num));
    }


}
