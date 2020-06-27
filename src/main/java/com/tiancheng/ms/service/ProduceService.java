package com.tiancheng.ms.service;

import com.tiancheng.ms.common.context.ContextHolder;
import com.tiancheng.ms.constant.MessageConstant;
import com.tiancheng.ms.dao.mapper.ProcessMapper;
import com.tiancheng.ms.dao.mapper.ProduceMapper;
import com.tiancheng.ms.dao.mapper.ProduceMsgMapper;
import com.tiancheng.ms.dao.mapper.ProduceProcessMapper;
import com.tiancheng.ms.dto.ProduceDetailDTO;
import com.tiancheng.ms.dto.ProduceProcessDTO;
import com.tiancheng.ms.entity.ProcessEntity;
import com.tiancheng.ms.entity.ProduceEntity;
import com.tiancheng.ms.entity.ProduceMsgEntity;
import com.tiancheng.ms.entity.ProduceProcessEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void addProduce(ProduceEntity entity) {
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        entity.setCreateBy(ContextHolder.getUser().getUserName());
        entity.setUpdateBy(ContextHolder.getUser().getUserName());
        entity.setCode(generiateProduceCode());
        produceMapper.insert(entity);

        List<ProcessEntity> processEntities = processMapper.getProduceProcess(entity.getProductCode());
        processEntities.stream().map(processEntity -> {
            ProduceProcessEntity produceProcessEntity = new ProduceProcessEntity();
            productProcessEntity.setproduc(processEntity.getId());
            productProcessEntity.setProductId();
        })
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
            switch (entity.getType()){
                case 1: {
                    entity.setContent(MessageConstant.CUSTOM_MESSAGE
                            .replace("{username}",ContextHolder.getUser().getUserName())
                            .replace("{content}",entity.getContent())
                    );
                    break;
                }
                case 2: {
                    entity.setContent(MessageConstant.ACCEPT_MESSAGE
                            .replace("{username}",ContextHolder.getUser().getUserName())
                            .replace("{amount}",entity.getAmount().toString())
                    );
                    break;
                }
                case 3: {
                    entity.setContent(MessageConstant.TRANSMIT_MESSAGE
                            .replace("{username}",ContextHolder.getUser().getUserName())
                            .replace("{process}",entity.getProcessName())
                            .replace("{amount}",entity.getAmount().toString())
                    );
                    break;
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
        return produceMapper.getProduceDetail(produceId);
    }



    private String generiateProduceCode() {
        String lastCode = produceMapper.getLastProduce();
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
