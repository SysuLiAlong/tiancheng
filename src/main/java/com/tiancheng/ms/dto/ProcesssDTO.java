package com.tiancheng.ms.dto;

import lombok.Data;

@Data
public class ProcesssDTO {

    private Integer id;

    private String name;

    private Integer chargeUserId;

    private String chargeUserName;

    private Integer priority;

    private Boolean enabled = true;

    private Integer type = 1;

}
