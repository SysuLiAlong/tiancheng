package com.tiancheng.ms.dto.param;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserQueryParam {

    private String userName;

    private String phone;

    private Integer id;

    private String token;
}
