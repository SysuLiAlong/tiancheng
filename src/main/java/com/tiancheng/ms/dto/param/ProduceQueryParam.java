package com.tiancheng.ms.dto.param;

import lombok.Data;

@Data
public class ProduceQueryParam {

    public String orderCode;

    public String orderParam;

    private Integer pageNo = 1;

    private Integer pageSize = 10;

    private String chargeUserName;
}
