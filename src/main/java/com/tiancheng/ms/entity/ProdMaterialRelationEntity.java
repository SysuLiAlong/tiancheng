package com.tiancheng.ms.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@ToString
@Table(name = "prod_material_relation")
public class ProdMaterialRelationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "prod_id")
    private Integer prodId;

    @Column(name = "material_id")
    private Integer materialId;

}
