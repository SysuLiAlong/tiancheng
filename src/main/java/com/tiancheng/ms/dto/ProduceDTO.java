package com.tiancheng.ms.dto;

import lombok.Data;

@Data
public class ProduceDTO {

    private Integer id;

    private String code;

    private String orderCode;

    private Integer status;

    private String description;

    private Integer enabled;

    private String comment;

}
