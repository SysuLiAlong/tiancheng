package com.tiancheng.ms.dao.mapper;

import com.tiancheng.ms.entity.ProductMaterial;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface ProdMaterialRelatMapper extends Mapper<ProductMaterial>,
        MySqlMapper<ProductMaterial>,
        IdsMapper<ProductMaterial> {
}
