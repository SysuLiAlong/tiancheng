package com.tiancheng.ms.dto;

import lombok.Data;

@Data
public class ProduceProductDetailDTO {

    private Integer id;

    private Integer produceId;

    private Integer productId;

    private Integer mount;

    private String productName;

    private String currentProcess;

    private String chargeUserName;

    private String code;

    private Integer numsPerStove;

    private Integer alertPercent;

    private Double weight;

    private String imageCode;

    private String description;

    private String createBy;

}
