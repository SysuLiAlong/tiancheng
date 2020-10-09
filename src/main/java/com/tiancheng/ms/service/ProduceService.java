package com.tiancheng.ms.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tiancheng.ms.common.context.ContextHolder;
import com.tiancheng.ms.common.dto.Page;
import com.tiancheng.ms.common.exception.BusinessException;
import com.tiancheng.ms.constant.*;
import com.tiancheng.ms.dao.mapper.*;
import com.tiancheng.ms.dto.ProduceDTO;
import com.tiancheng.ms.dto.ProduceProcessDTO;
import com.tiancheng.ms.dto.ProduceProductDTO;
import com.tiancheng.ms.dto.ProduceProductDetailDTO;
import com.tiancheng.ms.dto.param.*;
import com.tiancheng.ms.entity.*;
import com.tiancheng.ms.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProduceService {

    @Autowired
    private ProduceMapper produceMapper;

    @Autowired
    private ProduceMsgMapper produceMsgMapper;

    @Autowired
    private ProduceProcessMapper produceProcessMapper;

    @Autowired
    private ProduceProductMapper produceProductMapper;

    @Autowired
    private ProcessMapper processMapper;

    @Autowired
    private UserRoleConstant userRoleConstant;

    @Autowired
    private ProduceProcessConstant produceProcessConstant;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private AlarmService alarmService;


    @Value("${file.downLoadPath}")
    private String downLoadPath;

    public Page<ProduceDTO> pageQryProduceForAdmin(ProduceQueryParam queryParam) {
        PageHelper.startPage(queryParam.getPageNo(),queryParam.getPageSize());
        List<ProduceEntity> entities = produceMapper.pageQryProduceForAdmin(queryParam.getOrderCode(), queryParam.getOrderParam());
        List<ProduceDTO> dtos = BeanUtils.copy(entities, ProduceDTO.class);
        PageInfo pageInfo = new PageInfo(dtos);
        return new Page<>(pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal(), dtos);
    }

    public Page<ProduceDTO> pageQryProduceForCommonUser(ProduceQueryParam queryParam) {
        String chargeUserName = ContextHolder.getUser().getUserName();
        List<Integer> produceIds = produceProcessMapper.queryUnOverProduceIdsByChargeUserName(chargeUserName);
        List<ProduceEntity> entities = produceMapper.pageQryProduceForAdmin(queryParam.getOrderCode(), queryParam.getOrderParam());
        List<ProduceEntity> filterProduces = entities.stream().filter(entity -> {
            return produceIds.contains(entity.getId());
        }).collect(Collectors.toList());
        List<ProduceDTO> dtos = BeanUtils.copy(filterProduces, ProduceDTO.class);
        // 手动分页
        Integer beginIndex = queryParam.getPageNo() * queryParam.getPageSize();
        Integer endIndex = beginIndex + queryParam.getPageSize() <= filterProduces.size() ? beginIndex + queryParam.getPageSize() : filterProduces.size();
        List<ProduceDTO> pagedDtos = dtos.subList(beginIndex, endIndex);
        return new Page<>(queryParam.getPageNo(), queryParam.getPageSize(), (long) dtos.size(), pagedDtos);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addProduce(ProduceParam produceParam) {
        ProduceEntity entity = new ProduceEntity();
        BeanUtils.copy(produceParam,entity);
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        entity.setCreateBy(ContextHolder.getUser().getUserName());
        entity.setUpdateBy(ContextHolder.getUser().getUserName());
        entity.setCode(generiateProduceCode());
        produceMapper.insert(entity);

        if (CollectionUtils.isEmpty(produceParam.getProduceProductParams())) {
            return;
        }

        for (ProduceProductParam productParam : produceParam.getProduceProductParams()) {
            ProduceProductEntity productEntity = new ProduceProductEntity();
            BeanUtils.copy(productParam, productEntity);
            productEntity.setCreateBy(ContextHolder.getUser().getUserName());
            productEntity.setUpdateBy(ContextHolder.getUser().getUserName());
            productEntity.setCreateTime(new Date());
            productEntity.setUpdateTime(new Date());
            productEntity.setProduceId(entity.getId());
            produceProductMapper.insert(productEntity);
            List<ProcessEntity> processEntities = productMapper.getProcesses(productParam.getProductId());
            if (CollectionUtils.isEmpty(processEntities)) {
                continue;
            }

            List<ProduceProcessEntity> produceProcessEntities = new ArrayList<>(processEntities.size());
            //添加“结束”流程
            ProduceProcessEntity endProcess = new ProduceProcessEntity();
            endProcess.setProduceId(entity.getId());
            endProcess.setProcessId(produceProcessConstant.getEndId());
            endProcess.setProcessName(produceProcessConstant.getEndName());
            endProcess.setChargeUserName(produceProcessConstant.getUserName());
            endProcess.setStatus(ProduceProcessStatusConstant.INIT_STATUS);
            endProcess.setProduceProductId(productEntity.getId());
            endProcess.setProductId(productParam.getProductId());
            produceProcessEntities.add(endProcess);
            ProduceProcessEntity nextProcess = new ProduceProcessEntity();
            BeanUtils.copy(endProcess, nextProcess);
            for (int i= 0;i < processEntities.size(); i++) {
                ProcessEntity processEntity = processEntities.get(i);
                ProduceProcessEntity produceProcessEntity = new ProduceProcessEntity();
                produceProcessEntity.setProcessId(processEntity.getId());
                produceProcessEntity.setProduceId(entity.getId());
                produceProcessEntity.setProcessName(processEntity.getName());
                produceProcessEntity.setChargeUserName(processEntity.getChargeUserName());
                produceProcessEntity.setNextProcess(nextProcess.getProcessId());
                produceProcessEntity.setProduceProductId(productEntity.getId());
                produceProcessEntity.setProductId(productParam.getProductId());
                BeanUtils.copy(produceProcessEntity, nextProcess);
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
            startProcess.setProduceProductId(productEntity.getId());
            startProcess.setStartTime(new Date());
            startProcess.setEndTime(new Date());
            startProcess.setNextProcess(nextProcess.getProcessId());
            nextProcess.setStartNum(productEntity.getMount());
            startProcess.setStartNum(productEntity.getMount());
            startProcess.setEndNum(productEntity.getMount());
            startProcess.setProductId(productParam.getProductId());
            produceProcessEntities.add(startProcess);
            produceProcessMapper.insertList(produceProcessEntities);
        }
    }

    public void addProduceMsg(ProduceMsgParam param) {
        ProduceMsgEntity entity = new ProduceMsgEntity();
        BeanUtils.copy(param, entity);
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        entity.setCreateBy(ContextHolder.getUser().getUserName());
        entity.setUpdateBy(ContextHolder.getUser().getUserName());
        entity.setOperateUserName(ContextHolder.getUser().getUserName());
        produceMsgMapper.insert(entity);
    }

    public List<ProduceMsgEntity> listProduceMsg(Integer produceProductId) {
        List<ProduceMsgEntity> entities =  produceMsgMapper.listProduceMsg(produceProductId);
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
                    break;
                }
                case 5: {
                    // TODO: 2020/9/26 将链接转成link
                    entity.setContent(MessageConstant.UPLOAD_IMAGE_MESSAGE
                            .replace("{username}",createBy));
                    break;
                }
                default: entity.setContent("消息异常");
            }
        });
        return entities;
    }

    public List<ProduceProcessDTO> listProduceProcess(Integer produceProductId) {
        return produceProcessMapper.listProduceProcess(produceProductId);
    }

    public ProduceProductDetailDTO getProduceProductDetail(Integer produceProductId) {
        return produceProductMapper.queryProduceProduct(produceProductId);
   }

    @Transactional(rollbackFor = Exception.class)
    public void acceptProcess(ProduceProcessAndMsgParam param) {
        ProduceProcessEntity produceProcessEntity = produceProcessMapper.selectByPrimaryKey(param.getProduceProcessParam().getId());
        produceProcessEntity.setStatus(ProduceProcessStatusConstant.ACCEPT_STATUS);
        produceProcessMapper.updateByPrimaryKey(produceProcessEntity);

        ProduceProcessEntity nextProcess = produceProcessMapper.getNextProcess(param.getProduceProcessParam().getId());
        nextProcess.setStatus(ProduceProcessStatusConstant.START_STATUS);
        nextProcess.setStartTime(new Date());
        nextProcess.setStartNum(produceProcessEntity.getEndNum());
        produceProcessMapper.updateByPrimaryKey(nextProcess);

        addProduceMsg(param.getProduceMsgParam());
    }

    @Transactional(rollbackFor = Exception.class)
    public void rejectProcess(ProduceProcessAndMsgParam param) {
        ProduceProcessEntity produceProcessEntity = produceProcessMapper.selectByPrimaryKey(param.getProduceProcessParam().getId());
        produceProcessEntity.setStatus(ProduceProcessStatusConstant.START_STATUS);
        produceProcessEntity.setEndTime(null);
        produceProcessMapper.updateByPrimaryKey(produceProcessEntity);

        ProduceProcessEntity nextProcess = produceProcessMapper.getNextProcess(param.getProduceProcessParam().getId());
        nextProcess.setStatus(ProduceProcessStatusConstant.INIT_STATUS);
        nextProcess.setStartNum(null);
        nextProcess.setStartTime(null);
        produceProcessMapper.updateByPrimaryKey(nextProcess);

        addProduceMsg(param.getProduceMsgParam());
    }

    public ProduceProcessEntity getLastProcess(Integer produceProductId) {
        ProduceProcessEntity currentProcess = getCurrentProcess(produceProductId);
        if (currentProcess == null) {
            throw new BusinessException(ErrorCode.FAIL,"数据异常，无法查到当前流程,请重试！");
        }
        return produceProcessMapper.getLastProcess(produceProductId,currentProcess.getProcessId());
    }

    public ProduceProcessEntity getCurrentProcess(Integer produceProductId) {
        ProduceProcessEntity currentProcess =  produceProcessMapper.getCurrentProcess(produceProductId);
        if (currentProcess == null) {
            throw new BusinessException(ErrorCode.FAIL,"数据异常，无法查到当前流程,请重试！");
        }
        return currentProcess;
    }

    public ProduceProcessEntity getNextProcess(Integer produceProductId) {
        ProduceProcessEntity currentProcess = getCurrentProcess(produceProductId);
        if (currentProcess == null) {
            throw new BusinessException(ErrorCode.FAIL,"数据异常，无法查到当前流程,请重试！");
        }
        return produceProcessMapper.getNextProcess(currentProcess.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void transmitProcess(ProduceProcessAndMsgParam param) {
        ProduceProcessEntity produceProcessEntity = produceProcessMapper.selectByPrimaryKey(param.getProduceProcessParam().getId());
        produceProcessEntity.setStatus(ProduceProcessStatusConstant.END_STATUS);
        produceProcessEntity.setEndTime(new Date());
        produceProcessEntity.setEndNum(param.getProduceProcessParam().getEndNum());
        produceProcessMapper.updateByPrimaryKey(produceProcessEntity);

        ProduceProcessEntity nextProcess = produceProcessMapper.getNextProcess(param.getProduceProcessParam().getId());
        nextProcess.setStatus(ProduceProcessStatusConstant.ARRIVE_STATUS);
        nextProcess.setStartNum(param.getProduceProcessParam().getEndNum());
        produceProcessMapper.updateByPrimaryKey(nextProcess);

        addProduceMsg(param.getProduceMsgParam());

        ProductEntity productEntity = productMapper.selectByPrimaryKey(produceProcessEntity.getProductId());
        ProduceProductEntity produceProductEntity = produceProductMapper.selectByPrimaryKey(produceProcessEntity.getProduceProductId());
        if (produceProductEntity.getMount() * productEntity.getAlertPercent() < produceProcessEntity.getEndNum()) {
            AlarmParam alarmParam = new AlarmParam();
            alarmParam.setProcessId(produceProcessEntity.getProcessId());
            alarmParam.setProductId(productEntity.getId());
            alarmParam.setProduceId(produceProductEntity.getProduceId());
            alarmParam.setProduceProductId(produceProductEntity.getId());
            alarmParam.setType(AlarmParam.FAULT_RATE_ALARM);
            alarmService.addAlarm(alarmParam);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteProduce(Integer produceId) {
        produceMapper.deleteByPrimaryKey(produceId);
        produceProcessMapper.deleteByProduceId(produceId);
        produceMsgMapper.deleteByProduceId(produceId);
    }

    public List<ProduceProductDTO> listProduceProduct(Integer produceId) {
        return produceProductMapper.getProductsByProduceId(produceId);
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
