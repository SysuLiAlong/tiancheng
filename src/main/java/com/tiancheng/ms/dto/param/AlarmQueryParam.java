package com.tiancheng.ms.dto.param;

import lombok.Data;

@Data
public class AlarmQueryParam {
    private Integer pageNo;

    private Integer pageSize;

    private Integer type;
}
