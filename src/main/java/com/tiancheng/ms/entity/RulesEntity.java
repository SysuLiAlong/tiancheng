package com.tiancheng.ms.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@ToString
@Table(name = "rules")
public class RulesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "prod_id")
    private Integer prodId;

    @Column(name = "proc_id")
    private Integer procId;

    @Transient
    private String procName;

    private Integer intervals;
}
