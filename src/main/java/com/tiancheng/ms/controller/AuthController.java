package com.tiancheng.ms.controller;

import com.tiancheng.ms.common.dto.AjaxResult;
import com.tiancheng.ms.common.exception.BusinessException;
import com.tiancheng.ms.constant.ErrorCode;
import com.tiancheng.ms.constant.GlobalConstant;
import com.tiancheng.ms.dto.UserDTO;
import com.tiancheng.ms.dto.param.UserParam;
import com.tiancheng.ms.entity.UserEntity;
import com.tiancheng.ms.service.UserService;
import com.tiancheng.ms.util.CookieUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public AjaxResult<UserEntity> login(@RequestBody UserParam user,
                                     HttpServletResponse response) {
        if (user == null || StringUtils.isEmpty(user.getUserName()) || StringUtils.isEmpty(user.getPassword())) {
            throw new BusinessException(ErrorCode.FAIL_AUTH);
        }
        // TODO: 2020/3/7 password进行des加密处理
        UserEntity userEntity = userService.queryByUserName(user.getUserName());
        if(userEntity == null || !user.getPassword().equals(userEntity.getPassword())) {
            return AjaxResult.fail("用户名密码不正确");
        } else {
            UserDTO userDTO = new UserDTO();
            String newToken = UUID.randomUUID().toString();
            userService.updateToken(user.getUserName(),newToken);
            BeanUtils.copyProperties(userEntity,userDTO);
            CookieUtil.setCookieValue(response, GlobalConstant.TOKEN, newToken, 2 * 60);
            if (user.getRemember()) {
                CookieUtil.setCookieValue(response, GlobalConstant.USER_NAME, user.getUserName(), 30 * 24 * 60);
                CookieUtil.setCookieValue(response, GlobalConstant.PASS_WORD, user.getPassword(), 30 * 24 * 60);
            } else {
                CookieUtil.setCookieValue(response, GlobalConstant.USER_NAME, user.getUserName(), 0);
                CookieUtil.setCookieValue(response, GlobalConstant.PASS_WORD, user.getUserName(), 0);
            }
            return AjaxResult.success(userDTO);
        }
    }
}
