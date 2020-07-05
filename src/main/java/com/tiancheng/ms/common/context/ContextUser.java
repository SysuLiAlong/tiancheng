package com.tiancheng.ms.common.context;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

@Data
public class ContextUser implements Serializable {
    private Integer id;
    private String userName;
    private String realName;
    private String role;
    @JsonIgnore
    private String password;
}
