package com.tiancheng.ms.service;

import com.tiancheng.ms.common.context.ContextHolder;
import com.tiancheng.ms.common.exception.BusinessException;
import com.tiancheng.ms.constant.ErrorCode;
import com.tiancheng.ms.dao.mapper.MaterialTypeMapper;
import com.tiancheng.ms.dto.param.MaterialTypeParam;
import com.tiancheng.ms.entity.MaterialTypeEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

@Service
public class MaterialService {
    @Autowired
    private MaterialTypeMapper materialTypeMapper;

    public Integer addMaterialType(MaterialTypeParam param) {
        if(param.getName() == null) {
            throw new BusinessException(ErrorCode.FAIL,"类型名称不能为空");
        }
        Example example = new Example(MaterialTypeEntity.class);
        example.createCriteria().andEqualTo("name",param.getName());
        MaterialTypeEntity existEntity = materialTypeMapper.selectOneByExample(example);
        if(existEntity != null) {
            throw new BusinessException(ErrorCode.FAIL,"材料类型名称重复");
        }

        MaterialTypeEntity entity = new MaterialTypeEntity();
        BeanUtils.copyProperties(param,entity);
        entity.setUpdateBy(ContextHolder.getUser().getUserName());
        entity.setCreateBy(ContextHolder.getUser().getUserName());
        Date now = new Date();
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        materialTypeMapper.insertSelective(entity);
        return entity.getId();
    }
}
