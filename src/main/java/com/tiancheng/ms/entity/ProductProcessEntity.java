package com.tiancheng.ms.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@ToString
@Table(name = "product_process")
public class ProductProcessEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "process_id")
    private Integer processId;

    public ProductProcessEntity() {}

    public ProductProcessEntity(Integer productId, Integer processId) {
        this.productId = productId;
        this.processId = processId;
    }
}
