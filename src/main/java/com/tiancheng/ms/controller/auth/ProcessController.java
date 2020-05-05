package com.tiancheng.ms.controller.auth;

import com.tiancheng.ms.common.context.ContextHolder;
import com.tiancheng.ms.common.exception.BusinessException;
import com.tiancheng.ms.constant.ErrorCode;
import com.tiancheng.ms.dao.mapper.ProcessMapper;
import com.tiancheng.ms.dto.param.ProcessParam;
import com.tiancheng.ms.entity.ProcessEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/process")
public class ProcessController {

    @Autowired
    private ProcessMapper processMapper;

    @RequestMapping(value = "/add")
    public void addProcess(@RequestBody ProcessParam param) {
        Example example = new Example(ProcessEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name",param.getName());
        if(processMapper.selectOneByExample(example) != null) {
            throw new BusinessException(ErrorCode.FAIL,"名称重复");
        }
        ProcessEntity entity = new ProcessEntity();
        entity.setUpdateBy(ContextHolder.getUser().getUserName());
        entity.setCreateBy(ContextHolder.getUser().getUserName());
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());

        BeanUtils.copyProperties(param,entity);
        processMapper.insertSelective(entity);
    }

    @PostMapping(value = "update")
    public void updateProcess(@RequestBody ProcessParam param) {
        ProcessEntity entity = processMapper.selectByPrimaryKey(param.getId());
        if(entity == null) {
            throw new BusinessException(ErrorCode.FAIL,"修改流程不存在");
        }
        Example example = new Example(ProcessEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name",param.getName());
        ProcessEntity existEntity = processMapper.selectOneByExample(example);
        if(existEntity != null && existEntity.getId() != param.getId()) {
            throw new BusinessException(ErrorCode.FAIL,"名称重复");
        }
        BeanUtils.copyProperties(param,entity);
        entity.setUpdateTime(new Date());
        entity.setUpdateBy(ContextHolder.getUser().getUserName());
    }

    @RequestMapping("/list")
    public List<ProcessEntity> listProcess() {
        return processMapper.selectAll();
    }

    @RequestMapping("/delete/{id}")
    public void deleteProcess(@PathVariable(value = "id") Integer id) {
        ProcessEntity entity = processMapper.selectByPrimaryKey(id);
        if(entity == null) {
            throw new BusinessException(ErrorCode.FAIL,"删除内容不存在");
        }
        processMapper.deleteByPrimaryKey(id);
    }




}
