package com.tiancheng.ms.dto.param;

import com.tiancheng.ms.entity.ProductMaterial;
import com.tiancheng.ms.entity.ProductProcessEntity;
import com.tiancheng.ms.entity.RulesEntity;
import lombok.Data;

import java.util.List;

@Data
public class ProductDetailParam {

    private ProductParam productParam;

    private List<ProductProcessEntity> processParams;

    private List<ProductMaterial> materialParams;

    private List<RulesEntity> ruleParams;
}
