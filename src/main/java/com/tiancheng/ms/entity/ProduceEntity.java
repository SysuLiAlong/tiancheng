package com.tiancheng.ms.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Data
@ToString
@Table(name = "produce")
public class ProduceEntity {

    @Transient
    public static Integer PRODUCE_COMPLETE_STATUS = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String code;

    @Column(name = "order_code")
    private String orderCode;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "update_by")
    private String updateBy;

    private Integer status = PRODUCE_COMPLETE_STATUS;

    private String description;

    private Boolean enabled = true;

    private String comment;

}
