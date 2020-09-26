package com.tiancheng.ms.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Data
@ToString
@Table(name = "produce_process")
public class ProduceProcessEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "produce_id")
    private Integer produceId;

    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "produce_product_id")
    private Integer produceProductId;

    @Column(name = "process_id")
    private Integer processId;

    @Column(name = "status")
    private Integer status;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "start_num")
    private Integer startNum;

    @Column(name = "end_num")
    private Integer endNum;

    @Column(name = "charge_user_name")
    private String ChargeUserName;

    @Column(name = "process_name")
    private String processName;

    @Column(name = "next_process")
    private Integer nextProcess;
}
