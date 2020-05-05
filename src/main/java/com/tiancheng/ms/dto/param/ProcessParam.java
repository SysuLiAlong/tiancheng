package com.tiancheng.ms.dto.param;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ProcessParam {

    private Integer id;

    private String name;

    private Integer chargeUserId;

    private String chargeUserName;
}
