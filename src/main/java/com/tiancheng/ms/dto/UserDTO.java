package com.tiancheng.ms.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tiancheng.ms.common.context.ContextUser;
import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {
    private Integer id;

    private String userName;

    private String realName;

    @JsonIgnore
    private String password;

    private String phone;

    private String token;

    private Date createTime;

    private Date updateTime;

    private String createBy;

    private String updateBy;

    private Boolean enabled;

    private Integer roleId;

    private String roleName;

    public ContextUser convertToContextUser() {
        return new ContextUser(id, userName, realName, password, phone);
    }
}
