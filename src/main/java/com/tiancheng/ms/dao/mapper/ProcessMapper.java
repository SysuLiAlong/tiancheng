package com.tiancheng.ms.dao.mapper;

import com.tiancheng.ms.entity.ProcessEntity;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ProcessMapper extends Mapper<ProcessEntity> {

    List<ProcessEntity> selectByProductId(@Param("productId") Integer productId);

}
