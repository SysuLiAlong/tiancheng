package com.tiancheng.ms.dto.param;

import lombok.Data;

@Data
public class UserParam {
    private Integer id;

    private String userName;

    private String realName;

    private String role;

    private String roleName;

    private String password;
}
