package com.tiancheng.ms.controller.auth;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tiancheng.ms.common.context.ContextHolder;
import com.tiancheng.ms.common.dto.Page;
import com.tiancheng.ms.common.dto.SelectOption;
import com.tiancheng.ms.common.exception.BusinessException;
import com.tiancheng.ms.constant.ErrorCode;
import com.tiancheng.ms.constant.ExampleConstant;
import com.tiancheng.ms.dao.mapper.MaterialMapper;
import com.tiancheng.ms.dao.mapper.MaterialTypeMapper;
import com.tiancheng.ms.dto.param.MaterialParam;
import com.tiancheng.ms.dto.param.MaterialQueryParam;
import com.tiancheng.ms.dto.param.MaterialTypeParam;
import com.tiancheng.ms.entity.MaterialEntity;
import com.tiancheng.ms.entity.MaterialTypeEntity;
import com.tiancheng.ms.service.MaterialService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/material")
public class MaterialController {
    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private MaterialTypeMapper materialTypeMapper;

    @Autowired
    private MaterialService materialService;

    @RequestMapping("/add")
    @Transactional(rollbackFor = Exception.class)
    public void addMaterial(@RequestBody MaterialParam param) {
        if(param.getCode() == null) {
            throw new BusinessException(ErrorCode.FAIL,"编码不能为空");
        }
        Example example = new Example(MaterialEntity.class);
        example.createCriteria().andEqualTo("code",param.getCode());
        MaterialEntity existEntity = materialMapper.selectOneByExample(example);
        if(existEntity != null) {
            throw new BusinessException(ErrorCode.FAIL,"编码重复");
        }

        if(param.getType() == -1) {
            MaterialTypeParam typeParam = new MaterialTypeParam();
            typeParam.setName(param.getTypeName());
            Integer materialTypeId = materialService.addMaterialType(typeParam);
            param.setType(materialTypeId);
        }

        MaterialEntity entity = new MaterialEntity();
        BeanUtils.copyProperties(param,entity);
        entity.setUpdateBy(ContextHolder.getUser().getUserName());
        entity.setCreateBy(ContextHolder.getUser().getUserName());
        Date now = new Date();
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        materialMapper.insertSelective(entity);
    }

    @RequestMapping("/page_qry")
    public Page<MaterialEntity> pageQryMaterial(@RequestBody MaterialQueryParam queryParam) {
        Example example = new Example(MaterialEntity.class);
        Example.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(queryParam.getCode()) && queryParam.getCode().trim() != "") {
            criteria.andLike("code",queryParam.getCode().trim());
        }
        PageHelper.startPage(queryParam.getPageNo(),queryParam.getPageSize());
        List<MaterialEntity> entities = materialMapper.selectByExample(example);
        PageInfo pageInfo = new PageInfo(entities);
        return new Page<>(pageInfo.getPageNum(),pageInfo.getPageSize(),pageInfo.getTotal(),entities);
    }

    @RequestMapping("/list")
    public List<MaterialEntity> listMaterial(@RequestParam(value = "code",required = false) String code) {
        Example example = new Example(MaterialEntity.class);
        Example.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(code) && code.trim() != "") {
            criteria.andLike("code",code);
        }
        return materialMapper.selectByExample(example);
    }

    @PostMapping("/delete/{id}")
    public void deleteMaterial(@PathVariable Integer id) {
        materialMapper.deleteByPrimaryKey(id);
    }



    @RequestMapping("/type/add")
    public void addMaterialType(@RequestBody MaterialTypeParam param) {
        materialService.addMaterialType(param);
    }

    @RequestMapping(value = "/type/options")
    List<SelectOption> typeOptions() {
        List<MaterialTypeEntity> entities = materialTypeMapper.selectAll();
        return entities.stream().map(entity -> {
            return new SelectOption(entity.getId().toString(), entity.getName());
        }).collect(Collectors.toList());
    }

    @RequestMapping(value = "/options")
    List<SelectOption> options(@RequestParam Integer typeId) {
        Example example = new Example(MaterialEntity.class);
        example.createCriteria().andEqualTo(ExampleConstant.MATERIAL_TYPE_ID, typeId);
        List<MaterialEntity> entities = materialMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(entities)) {
            return new ArrayList<>(0);
        }
        return entities.stream()
                .map(entity -> new SelectOption(String.valueOf(entity.getId()), entity.getCode()))
                .collect(Collectors.toList());
    }




}
