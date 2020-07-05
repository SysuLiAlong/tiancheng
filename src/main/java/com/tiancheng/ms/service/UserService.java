package com.tiancheng.ms.service;

import com.tiancheng.ms.common.context.ContextHolder;
import com.tiancheng.ms.common.context.ContextUser;
import com.tiancheng.ms.common.exception.BusinessException;
import com.tiancheng.ms.constant.ErrorCode;
import com.tiancheng.ms.dao.mapper.UserMapper;
import com.tiancheng.ms.dto.param.ChangePasswdParam;
import com.tiancheng.ms.dto.param.UserParam;
import com.tiancheng.ms.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public ContextUser getUserByToken(String token) {
        UserEntity entity = userMapper.selectUserByToken(token);
        if(entity == null) {
            return null;
        }
        ContextUser contextUser = new ContextUser();
        BeanUtils.copyProperties(entity,contextUser);
        return contextUser;
    }

    public void updateToken(String name, String token) {
        UserEntity entity = queryByUserName(name);
        entity.setToken(token);
        userMapper.updateByPrimaryKeySelective(entity);
    }

    public void changePassword(String newPassword) {
        ContextUser contextUser = ContextHolder.getUser();
        UserEntity entity = userMapper.selectByPrimaryKey(contextUser.getId());
        entity.setPassword(newPassword);
        entity.setUpdateTime(new Date());
        entity.setUpdateBy(contextUser.getUserName());
        userMapper.updateByPrimaryKeySelective(entity);
    }

    public UserEntity queryByUserName(String userName) {
        if (StringUtils.isEmpty(userName.trim())) {
            return null;
        }
        return userMapper.selectByUserName(userName);
    }

    public void resetPasswd(ChangePasswdParam passwdParam) {
        userMapper.resetPasswd(passwdParam.getUserId(),passwdParam.getNewPassword());
    }


    public void addUser(UserParam param) {
        UserEntity userEntity = userMapper.selectByUserName(param.getUserName());
        if (userEntity != null) {
            throw new BusinessException(ErrorCode.FAIL,"用户名重复！");
        }
        userEntity = new UserEntity();
        BeanUtils.copyProperties(param,userEntity);
        userEntity.setCreateTime(new Date());
        userEntity.setUpdateTime(new Date());
        userEntity.setCreateBy(ContextHolder.getUser().getUserName());
        userEntity.setUpdateBy(ContextHolder.getUser().getUserName());
        userMapper.insertSelective(userEntity);


    }

    public void updateUser(UserParam param) {
        UserEntity entity = userMapper.selectByUserName(param.getUserName());
        boolean isUserNameExist = entity != null && entity.getId() != param.getId();
        if (isUserNameExist) {
            throw new BusinessException(ErrorCode.FAIL,"用户名重复");
        }
        BeanUtils.copyProperties(param,entity);
        entity.setUpdateBy(ContextHolder.getUser().getUserName());
        entity.setUpdateTime(new Date());
        userMapper.updateByPrimaryKeySelective(entity);
    }
}
