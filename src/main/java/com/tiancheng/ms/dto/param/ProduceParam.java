package com.tiancheng.ms.dto.param;

import com.tiancheng.ms.entity.ProduceEntity;
import lombok.Data;

@Data
public class ProduceParam extends ProduceEntity {
    private String content;
}
