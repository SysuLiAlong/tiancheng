package com.tiancheng.ms.dto.param;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserParam {

    private Integer id;

    private String userName;

    private String realName;

    private String phone;

}
