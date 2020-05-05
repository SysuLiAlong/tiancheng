package com.tiancheng.ms.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Data
@ToString
@Table(name = "prod_proc_relation")
public class ProdProcRelationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "prod_id")
    private Integer prodId;

    @Column(name = "proc_id")
    private Integer procId;

    private Boolean enabled;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "start_nums")
    private Integer startNums;

    @Column(name = "end_nums")
    private Integer endNums;

    public ProdProcRelationEntity () {}

    public ProdProcRelationEntity (Integer prodId, Integer procId) {
        this.prodId = prodId;
        this.procId = procId;
    }
}
