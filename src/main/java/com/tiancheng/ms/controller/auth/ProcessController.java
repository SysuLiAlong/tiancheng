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

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/process")
public class ProcessController {

    @Autowired
    private ProcessMapper processMapper;

    @RequestMapping(value = "/add")
    public synchronized void addProcess(@RequestBody ProcessParam param) {
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
        BeanUtils.copyProperties(param,entity);
        processMapper.insertSelective(entity);
    }

    @PostMapping(value = "update")
    public void updateProcess(@RequestBody ProcessParam param) {
        ProcessEntity entity = processMapper.selectByPrimaryKey(param.getId());
        if(entity == null) {
            throw new BusinessException(ErrorCode.FAIL,"修改流程不存在");
        }
        boolean isNameExist = processMapper.selectOneByName(param.getName()) == null ? false : true;
        if (isNameExist) {
            throw new BusinessException(ErrorCode.FAIL,"流程名称重复！");
        }
        BeanUtils.copyProperties(param,entity);
        entity.setUpdateTime(new Date());
        entity.setUpdateBy(ContextHolder.getUser().getUserName());
    }

    @RequestMapping("/list")
    public List<ProcessEntity> listProcess() {
        return processMapper.listPriorityProcess();
    }

    @RequestMapping("/delete/{id}")
    public void deleteProcess(@PathVariable(value = "id") Integer id) {
        ProcessEntity entity = processMapper.selectByPrimaryKey(id);
        if(entity == null) {
            throw new BusinessException(ErrorCode.FAIL,"删除内容不存在");
        }
        processMapper.deleteByPrimaryKey(id);
    }

    @RequestMapping("/exchange/priority")
    public void exchangePriority(@RequestParam(name = "first") Integer first, @RequestParam(name = "second") Integer second) {
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


}
