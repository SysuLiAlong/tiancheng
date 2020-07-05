package com.tiancheng.ms.constant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "process")
@Component
@Data
public class ProduceProcessConstant {

    private Integer startId;

    private Integer endId;

    private String userName;

    private String realName;

    private String startName;

    private String endName;

}
