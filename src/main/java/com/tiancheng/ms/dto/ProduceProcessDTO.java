package com.tiancheng.ms.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ProduceProcessDTO {

    private Integer id;

    private Integer produceId;

    private Integer productId;

    private Integer produceProductId;

    private Integer processId;

    private Integer status;

    private Date startTime;

    private Date endTime;

    private Integer startNum;

    private Integer endNum;

    private String ChargeUserName;

    private String processName;
}
