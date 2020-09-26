package com.tiancheng.ms.controller.auth;

import com.tiancheng.ms.common.context.ContextHolder;
import com.tiancheng.ms.common.context.ContextUser;
import com.tiancheng.ms.common.dto.SelectOption;
import com.tiancheng.ms.common.exception.BusinessException;
import com.tiancheng.ms.constant.ErrorCode;
import com.tiancheng.ms.constant.GlobalConstant;
import com.tiancheng.ms.dto.UserDTO;
import com.tiancheng.ms.dto.param.ChangePasswdParam;
import com.tiancheng.ms.dto.param.UserParam;
import com.tiancheng.ms.service.UserService;
import com.tiancheng.ms.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/all")
    public List<UserDTO> getUser(){
        return userService.getUser();
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
        return userService.userDetail(userId);
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
        userService.deleteUser(userId);
    }

    @GetMapping("/options")
    public List<SelectOption> userOptions() {
        return userService.userOptions();
    }

    @PostMapping("/logout")
    public void logout(HttpServletResponse response) {
        CookieUtil.setCookieValue(response, GlobalConstant.TOKEN, "", 0);
        CookieUtil.setCookieValue(response, GlobalConstant.USER_NAME, "", 0);
        CookieUtil.setCookieValue(response, GlobalConstant.PASS_WORD, "", 0);
    }


}
