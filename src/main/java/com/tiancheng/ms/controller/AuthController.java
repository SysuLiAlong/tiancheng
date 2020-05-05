package com.tiancheng.ms.controller;

import com.tiancheng.ms.common.dto.AjaxResult;
import com.tiancheng.ms.constant.GlobalConstant;
import com.tiancheng.ms.dto.UserDTO;
import com.tiancheng.ms.dto.param.UserQueryParam;
import com.tiancheng.ms.entity.RoleEntity;
import com.tiancheng.ms.service.UserService;
import com.tiancheng.ms.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public AjaxResult<UserDTO> login(@RequestParam(value = "userName") String userName,
                                     @RequestParam(value = "password") String password,
                                     HttpServletResponse response) {
        // TODO: 2020/3/7 password进行des加密处理
        List<UserDTO> userDTOS = userService.queryUserByParam(UserQueryParam.builder().userName(userName).build(),true);
        if(CollectionUtils.isEmpty(userDTOS)) {
            return AjaxResult.fail("用户名不存在");
        }
        UserDTO userDTO = userDTOS.get(0);
        if(userDTO.getPassword().equals(password)) {
            String newToken = UUID.randomUUID().toString();
            userService.updateToken(userName,newToken);
            RoleEntity role = userService.queryRole(userDTO.getId());
            userDTO.setRoleId(role.getId());
            userDTO.setRoleName(role.getName());
            CookieUtil.setCookieValue(response, GlobalConstant.TOKEN, newToken);
            return AjaxResult.success(userDTO);
        } else {
            return AjaxResult.fail("密码错误");
        }
    }
}
