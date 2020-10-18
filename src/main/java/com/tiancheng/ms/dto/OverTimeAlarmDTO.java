package com.tiancheng.ms.dto;

import lombok.Data;

import java.util.Date;

@Data
public class OverTimeAlarmDTO {

    private Integer produceId;

    private String produceCode;

    private String productCode;

    private Integer productId;

    private String processName;

    private Integer processId;

    private Date startTime;

    private Integer intervals;

}
