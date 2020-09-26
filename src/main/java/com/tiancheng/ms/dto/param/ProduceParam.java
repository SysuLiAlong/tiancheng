package com.tiancheng.ms.dto.param;

import lombok.Data;

import java.util.List;

@Data
public class ProduceParam {

    private Integer id;

    private String orderCode;

    private String description;

    private List<ProduceProductParam> produceProductParams;
}
