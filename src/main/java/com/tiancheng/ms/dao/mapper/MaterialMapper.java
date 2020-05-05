package com.tiancheng.ms.dao.mapper;

import com.tiancheng.ms.entity.MaterialEntity;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface MaterialMapper extends Mapper<MaterialEntity> {

    List<MaterialEntity> selectByProductId(@Param("productId") Integer productId);

}
