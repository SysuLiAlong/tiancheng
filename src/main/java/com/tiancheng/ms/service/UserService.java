package com.tiancheng.ms.service;

import com.tiancheng.ms.common.context.ContextHolder;
import com.tiancheng.ms.common.context.ContextUser;
import com.tiancheng.ms.common.dto.SelectOption;
import com.tiancheng.ms.common.exception.BusinessException;
import com.tiancheng.ms.constant.ErrorCode;
import com.tiancheng.ms.dao.mapper.RoleMapper;
import com.tiancheng.ms.dao.mapper.UserMapper;
import com.tiancheng.ms.dto.UserDTO;
import com.tiancheng.ms.dto.param.UserParam;
import com.tiancheng.ms.dto.param.UserQueryParam;
import com.tiancheng.ms.entity.RoleEntity;
import com.tiancheng.ms.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;

    public List<UserEntity> getUser(){

        log.info("info");
        log.debug("debug");
        log.warn("warn");
        log.error("error");
        return userMapper.selectAll();
    }

    public Integer addUser(UserParam param) {
        UserEntity entity = new UserEntity();
        BeanUtils.copyProperties(param,entity);
        entity.setCreateBy(ContextHolder.getUser().getUserName());
        entity.setUpdateBy(ContextHolder.getUser().getUserName());
        Date now = new Date();
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        entity.setEnabled(true);
        userMapper.insertSelective(entity);
        return entity.getId();
    }

    public UserDTO getUserByToken(String token) {
        Example example = new Example(UserEntity.class);
        example.createCriteria().andEqualTo("token",token);
        UserEntity entity = userMapper.selectOneByExample(example);
        if(entity == null) {
            throw new BusinessException(ErrorCode.FAIL_AUTH);
        }
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(entity,dto);
        return dto;
    }

    public void updateToken(String name, String token) {
        Example example = new Example(UserEntity.class);
        example.createCriteria().andEqualTo("userName",name);
        UserEntity entity = userMapper.selectOneByExample(example);
        if(entity == null) {
            throw new BusinessException(ErrorCode.FAIL_AUTH);
        }
        entity.setToken(token);
        userMapper.updateByPrimaryKeySelective(entity);
    }

    public List<UserDTO> queryUserByParam(UserQueryParam queryParam, Boolean exact) {
        Example example = new Example(UserEntity.class);
        Example.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(queryParam.getUserName())) {
            if(exact) {
                criteria.andEqualTo("userName",queryParam.getUserName());
            } else {
                criteria.andLike("userName",queryParam.getUserName());
            }
        }
        if(!StringUtils.isEmpty(queryParam.getPhone())) {
            criteria.andEqualTo("phone",queryParam.getPhone());
        }
        if(!StringUtils.isEmpty(queryParam.getToken())) {
            criteria.andEqualTo("token",queryParam.getToken());
        }
        if(queryParam.getId() != null) {
            criteria.andEqualTo("id",queryParam.getId());
        }

        List<UserEntity> entities = userMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(entities)) {
            return new ArrayList<>(0);
        }
        return entities.stream().map(entity -> {
            UserDTO dto = new UserDTO();
            BeanUtils.copyProperties(entity,dto);
            return dto;
        }).collect(Collectors.toList());
    }

    public RoleEntity queryRole(Integer userId) {
        return roleMapper.queryRoleOfUser(userId);
    }

    public void changePassword(String newPassword) {
        ContextUser contextUser = ContextHolder.getUser();
        UserEntity entity = userMapper.selectByPrimaryKey(contextUser.getId());
        entity.setPassword(newPassword);
        entity.setUpdateTime(new Date());
        entity.setUpdateBy(contextUser.getUserName());
        userMapper.updateByPrimaryKeySelective(entity);
    }

    public List<SelectOption> userOptions(Integer roleId) {
        List<UserEntity> entities = userMapper.queryByRoleId(roleId);
        return entities.stream()
                .map(entity -> new SelectOption(entity.getId().toString(),entity.getUserName()))
                .collect(Collectors.toList());
    }
}
