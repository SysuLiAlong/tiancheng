package com.tiancheng.ms.dto.param;

import lombok.Data;

@Data
public class MaterialQueryParam {

    private Integer pageNo;

    private Integer pageSize;

    private String code;
}
