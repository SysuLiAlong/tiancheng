package com.tiancheng.ms.controller.auth;

import com.tiancheng.ms.common.context.ContextHolder;
import com.tiancheng.ms.common.context.ContextUser;
import com.tiancheng.ms.common.exception.BusinessException;
import com.tiancheng.ms.constant.ErrorCode;
import com.tiancheng.ms.dao.mapper.UserMapper;
import com.tiancheng.ms.dto.UserDTO;
import com.tiancheng.ms.dto.param.ChangePasswdParam;
import com.tiancheng.ms.dto.param.UserParam;
import com.tiancheng.ms.entity.UserEntity;
import com.tiancheng.ms.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/all")
    public List<UserDTO> getUser(){
        List<UserEntity> userEntities =  userMapper.selectAll();
        return userEntities.stream().map(userEntity -> {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(userEntity,userDTO);
            return userDTO;
        }).collect(Collectors.toList());
    }

    @PostMapping("/change_password")
    public void changePassword(@RequestBody ChangePasswdParam passwdParam) {
        if (passwdParam == null || StringUtils.isEmpty(passwdParam.getNewPassword())
                || StringUtils.isEmpty(passwdParam.getOldPassword())) {
            throw new BusinessException(ErrorCode.PARAM_ERR);
        }
        ContextUser user = ContextHolder.getUser();
        if(user.getPassword().equals(passwdParam.getOldPassword())) {
            userService.changePassword(passwdParam.getNewPassword());
        } else {
            throw new BusinessException(ErrorCode.FAIL,"密码校验失败");
        }
    }

    @PostMapping("/reset_password")
    public void resetPassword(@RequestBody ChangePasswdParam passwdParam) {
        if (passwdParam == null || StringUtils.isEmpty(passwdParam.getNewPassword()) || passwdParam.getUserId() == null) {
            throw new BusinessException(ErrorCode.PARAM_ERR);
        }
        userService.resetPasswd(passwdParam);
    }

    @PostMapping("/add")
    public void addUser(@RequestBody UserParam param) {
        if (param == null || StringUtils.isEmpty(param.getUserName())
            || StringUtils.isEmpty(param.getRealName()) || StringUtils.isEmpty(param.getRole())
            || StringUtils.isEmpty(param.getPassword())) {
            throw new BusinessException(ErrorCode.PARAM_ERR);
        }
        userService.addUser(param);
    }

    @GetMapping("/detail/{userId}")
    public UserDTO userDetail(@PathVariable Integer userId) {
        UserEntity entity =  userMapper.selectByPrimaryKey(userId);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(entity,userDTO);
        return userDTO;
    }

    @RequestMapping("/update")
    public void updateUser(@RequestBody UserParam param) {
        if (param == null || param.getId() == null || StringUtils.isEmpty(param.getUserName())
            || StringUtils.isEmpty(param.getRealName()) || StringUtils.isEmpty(param.getRole())) {
            throw new BusinessException(ErrorCode.PARAM_ERR);
        }
        userService.updateUser(param);
    }

    @PostMapping("/delete/{userId}")
    public void deleteUser(@PathVariable Integer userId) {
        userMapper.deleteByPrimaryKey(userId);
    }


}
