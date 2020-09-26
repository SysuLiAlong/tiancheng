package com.tiancheng.ms.dto.param;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString
public class ProductParam {

    private Integer id;

    @NotNull(message = "产品编码不能为空")
    private String code;

    @NotNull(message = "产品名称不能为空")
    private String name;

    private Integer numsPerStove;

    private Integer alertPercent;

    private String imageCode;

    private Double weight;

    private String description;
}
