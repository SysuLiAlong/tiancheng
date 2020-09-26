package com.tiancheng.ms.common.dto;

import lombok.Data;

import java.util.List;

@Data
public class Page<T> {

    private Integer pageNo;

    private Integer pageSize;

    private Long total;

    private List<T> data;

    public Page() {

    }

    public Page(Integer pageNo, Integer pageSize, Long total, List<T> data) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.total = total;
        this.data = data;
    }

}
