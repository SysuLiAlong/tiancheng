package com.tiancheng.ms.controller.auth;

import com.alibaba.fastjson.JSONObject;
import com.tiancheng.ms.common.aop.ApiProperties;
import com.tiancheng.ms.common.context.ContextHolder;
import com.tiancheng.ms.common.context.ContextUser;
import com.tiancheng.ms.common.dto.SelectOption;
import com.tiancheng.ms.common.exception.BusinessException;
import com.tiancheng.ms.constant.ErrorCode;
import com.tiancheng.ms.entity.UserEntity;
import com.tiancheng.ms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.util.Assert;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private ApiProperties apiProperties;

    @RequestMapping("/all")
    public List<UserEntity> getUser(){
        return userService.getUser();
    }

    @PostMapping("/change_password")
    public void changePassword(@RequestBody JSONObject password) {
        Assert.notNull(password.getString("oldPassword"),"参数为空");
        Assert.notNull(password.getString("newPassword"),"参数为空");
        ContextUser user = ContextHolder.getUser();
        if(user.getPassword().equals(password.getString("oldPassword"))) {
            userService.changePassword(password.getString("newPassword"));
        } else {
            throw new BusinessException(ErrorCode.FAIL,"密码不正确");
        }
    }

    @GetMapping("/options")
    public List<SelectOption> userOptions(@RequestParam(value = "roleId",required = false) Integer roleId) {
        return userService.userOptions(roleId);
    }
}
