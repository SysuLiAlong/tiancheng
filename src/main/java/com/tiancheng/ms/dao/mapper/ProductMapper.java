package com.tiancheng.ms.dao.mapper;

import com.tiancheng.ms.dto.param.ProductQueryParam;
import com.tiancheng.ms.entity.ProcessEntity;
import com.tiancheng.ms.entity.ProductEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ProductMapper extends Mapper<ProductEntity> {
    @Select("select * from product where code = #{code} limit 1")
    ProductEntity selectOneByCode(@Param("code") String code);

    List<ProductEntity> queryByParam(@Param("queryParam") ProductQueryParam queryParam);

    List<ProcessEntity> getProcesses(@Param("productId") Integer productId);


    @Select("select * from product where code = #{code}")
    ProductEntity queryByCode(@Param("code") String code);
}
