package com.tiancheng.ms.common.context;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

@Data
public class ContextUser implements Serializable {
    private Integer id;
    private String userName;
    private String realName;
    private String phone;
    @JsonIgnore
    private String password;

    public ContextUser(){

    }

    public ContextUser(Integer id, String userName, String realName, String password, String phone) {
        this.id = id;
        this.userName = userName;
        this.realName = realName;
        this.password = password;
        this.phone = phone;
    }
}
