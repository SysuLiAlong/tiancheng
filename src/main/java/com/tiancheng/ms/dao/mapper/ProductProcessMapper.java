package com.tiancheng.ms.dao.mapper;

import com.tiancheng.ms.entity.ProductProcessEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;

public interface ProductProcessMapper extends Mapper<ProductProcessEntity>,
        MySqlMapper<ProductProcessEntity>,
        IdsMapper<ProductProcessEntity> {

    @Select("select * from product_process where product_id = #{productId}")
    List<ProductProcessEntity> selectProcessByProductId(@Param("productId") Integer productId);

    @Select("select * from product_process where process_id = #{processId}")
    List<ProductProcessEntity> selectByProcessId(@Param("processId") Integer processId);
}
