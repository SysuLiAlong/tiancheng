package com.tiancheng.ms.dto.param;

import lombok.Data;

@Data
public class ProduceMsgParam {

    private Integer id;

    private Integer produceId;

    private Integer productId;

    private Integer produceProductId;

    private String content;

    private String filePath;

    private Integer type;

    private String operateUserName;

    private String processName;

    private Integer amount;
}
