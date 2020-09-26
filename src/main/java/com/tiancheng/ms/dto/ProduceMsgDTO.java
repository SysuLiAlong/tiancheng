package com.tiancheng.ms.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ProduceMsgDTO {

    private Integer id;

    private Integer produceId;

    private Integer productId;

    private String content;

    private String filePath;

    private Date createTime;

    private Date updateTime;

    private String createBy;

    private String updateBy;

    private Integer type;

    private String operateUserName;

    private String processName;

    private Integer amount;
}
