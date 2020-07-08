package com.tiancheng.ms.dto;

import com.tiancheng.ms.entity.ProduceProcessEntity;
import lombok.Data;

@Data
public class ProduceProcessDTO extends ProduceProcessEntity {

    private String ChargeUserName;

    private String processName;

    private String produceCode;
}
