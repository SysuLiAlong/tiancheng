package com.tiancheng.ms.dao.mapper;

import com.tiancheng.ms.dto.ProduceProductDTO;
import com.tiancheng.ms.dto.ProduceProductDetailDTO;
import com.tiancheng.ms.entity.ProduceProductEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ProduceProductMapper extends Mapper<ProduceProductEntity>  {

    List<ProduceProductDTO> getProductsByProduceId(@Param("produceId") Integer produceId);

    ProduceProductDetailDTO queryProduceProduct(@Param("produceProductId") Integer produceProductId);

    @Select("select * from produce_product where product_id = #{productId}")
    List<ProduceProductEntity> getProducesByProductId(@Param("productId") Integer productId);

    List<ProduceProductEntity> getUnoverProducesByProductId(@Param("productId") Integer productId);
}
