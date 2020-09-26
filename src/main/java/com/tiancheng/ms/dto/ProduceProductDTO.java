package com.tiancheng.ms.dto;

import lombok.Data;

@Data
public class ProduceProductDTO {

    private Integer id;

    private Integer produceId;

    private Integer productId;

    private Integer mount;

    private String productName;

    private String productCode;

    private String currentProcess;

    private String chargeUserName;

}
