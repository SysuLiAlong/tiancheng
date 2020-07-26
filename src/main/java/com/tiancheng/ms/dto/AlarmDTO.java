package com.tiancheng.ms.dto;

import com.tiancheng.ms.entity.AlarmEntity;
import lombok.Data;

@Data
public class AlarmDTO extends AlarmEntity {

    private String produceCode;

    private String productCode;

    private String processName;

}
