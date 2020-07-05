package com.tiancheng.ms.dao.mapper;

import com.tiancheng.ms.entity.ProductEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface ProductMapper extends Mapper<ProductEntity> {
    @Select("select * from product where code = #{code} limit 1")
    ProductEntity selectOneByCode(@Param("code") String code);
}
