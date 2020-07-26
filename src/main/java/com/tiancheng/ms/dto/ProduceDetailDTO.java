package com.tiancheng.ms.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ProduceDetailDTO {

    private Integer id;

    private String code;

    private String orderCode;

    private String productCode;

    private Integer stove;

    private Date createTime;

    private Date updateTime;

    private String createBy;

    private String updateBy;

    private String produceProcessName;

    private String processChargeUserName;

    private Integer prdNums;

    private Integer alertNums;

    private String productName;

}
