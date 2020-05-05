package com.tiancheng.ms.dto;

import com.tiancheng.ms.entity.MaterialEntity;
import com.tiancheng.ms.entity.ProcessEntity;
import com.tiancheng.ms.entity.ProductEntity;
import com.tiancheng.ms.entity.RulesEntity;
import lombok.Data;

import java.util.List;

@Data
public class ProductDetailDTO {

    private ProductEntity product;

    private List<MaterialEntity> materials;

    private List<ProcessEntity> processes;

    private List<RulesEntity> rules;

    public ProductDetailDTO () {

    }

    public ProductDetailDTO (ProductEntity product,
                             List<MaterialEntity> materialEntities,
                             List<ProcessEntity> processEntities,
                             List<RulesEntity> rulesEntities) {
        this.product = product;
        this.materials = materialEntities;
        this.processes = processEntities;
        this.rules = rulesEntities;
    }
}
