package com.tiancheng.ms.dto.param;

import com.tiancheng.ms.entity.ProdMaterialRelationEntity;
import com.tiancheng.ms.entity.ProdProcRelationEntity;
import com.tiancheng.ms.entity.RulesEntity;
import lombok.Data;

import java.util.List;

@Data
public class ProductDetailParam {

    private ProductParam productParam;

    private List<ProdProcRelationEntity> procRelationParams;

    private List<ProdMaterialRelationEntity> materialRelationParams;

    private List<RulesEntity> ruleParams;
}
