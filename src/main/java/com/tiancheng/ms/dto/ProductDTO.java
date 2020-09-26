package com.tiancheng.ms.dto;

import lombok.Data;

@Data
public class ProductDTO {

    private Integer id;

    private String code;

    private String name;

    private Integer numsPerStove;

    private Integer alertPercent;

    private Double weight;

    private String imageCode;

    private String description;

}
