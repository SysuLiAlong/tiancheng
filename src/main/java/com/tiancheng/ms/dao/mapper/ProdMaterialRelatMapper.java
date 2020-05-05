package com.tiancheng.ms.dao.mapper;

import com.tiancheng.ms.entity.ProdMaterialRelationEntity;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface ProdMaterialRelatMapper extends Mapper<ProdMaterialRelationEntity>,
        MySqlMapper<ProdMaterialRelationEntity>,
        IdsMapper<ProdMaterialRelationEntity> {
}
