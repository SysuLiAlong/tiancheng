package com.tiancheng.ms.dao.mapper;

import com.tiancheng.ms.entity.ProdProcRelationEntity;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface ProdProcRelatMapper extends Mapper<ProdProcRelationEntity>,
        MySqlMapper<ProdProcRelationEntity>,
        IdsMapper<ProdProcRelationEntity> {
}
