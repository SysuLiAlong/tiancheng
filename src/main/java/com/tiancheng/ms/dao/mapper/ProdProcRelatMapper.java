package com.tiancheng.ms.dao.mapper;

import com.tiancheng.ms.entity.ProductProcessEntity;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface ProdProcRelatMapper extends Mapper<ProductProcessEntity>,
        MySqlMapper<ProductProcessEntity>,
        IdsMapper<ProductProcessEntity> {
}
