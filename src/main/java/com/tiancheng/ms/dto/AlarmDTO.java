package com.tiancheng.ms.dto;

import lombok.Data;

import java.util.Date;

@Data
public class AlarmDTO {

    public static String OVER_TIME_CONTENT_TMPL = "{produceCode}中： {productName}在【{processName}】流程超时";

    public static String FAULT_RATE_CONTENT_TMPL = "{produceCode}中： {productName}次品率超过限制";

    private String produceCode = "";

    private String productName = "";

    private String processName = "";

    private Date createTime;

    private String content = "";

    private Integer type;
}
