package com.tiancheng.ms.dto.param;

import lombok.Data;

@Data
public class ChangePasswdParam {
    private Integer userId;

    private String oldPassword;

    private String newPassword;
}
