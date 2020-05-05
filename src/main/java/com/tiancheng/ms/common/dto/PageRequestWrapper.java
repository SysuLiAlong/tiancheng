package com.tiancheng.ms.common.dto;

import lombok.Data;

@Data
public class PageRequestWrapper<T> {

    private Integer pageNo = 1;

    private Integer pageSize = 10;

    private T queryParam;
}
