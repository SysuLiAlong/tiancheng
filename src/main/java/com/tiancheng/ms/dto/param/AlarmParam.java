package com.tiancheng.ms.dto.param;

import lombok.Data;

@Data
public class AlarmParam {
    public static Integer FAULT_RATE_ALARM = 2;

    public static Integer OVER_TIME_ALARM = 1;

    private Integer id;

    private Integer produceId;

    private Integer processId;

    private Integer productId;

    private Integer type;

    private Boolean resolved;

    private String content;

    private Integer produceProductId;
}
