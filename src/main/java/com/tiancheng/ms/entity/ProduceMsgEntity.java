package com.tiancheng.ms.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Data
@ToString
@Table(name = "produce_msg")
public class ProduceMsgEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "produce_id")
    private Integer produceId;

    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "produce_product_id")
    private Integer produceProductId;

    @Column(name = "content")
    private String content;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "create_time",updatable = false)
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "update_by")
    private String updateBy;

    private Integer type;

    @Column(name = "operate_user_name")
    private String operateUserName;

    @Column(name = "process_name")
    private String processName;

    private Integer amount;

}
