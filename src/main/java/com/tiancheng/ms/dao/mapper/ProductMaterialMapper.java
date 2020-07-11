package com.tiancheng.ms.dao.mapper;

import com.tiancheng.ms.entity.ProductMaterial;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;

public interface ProductMaterialMapper extends Mapper<ProductMaterial>,
        MySqlMapper<ProductMaterial>,
        IdsMapper<ProductMaterial> {
    @Delete("delete from product_material where product_id = #{productId}")
    void deleteByProductId(@Param("productId") Integer productId);

    @Select("select * from product_material where material_id = #{materialId}")
    List<ProductMaterial> selectByMaterialId(@Param("materialId") Integer materialId);
}
