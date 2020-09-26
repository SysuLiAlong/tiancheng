package com.tiancheng.ms.dao.mapper;

import com.tiancheng.ms.entity.ProduceEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ProduceMapper extends Mapper<ProduceEntity> {
    @Select("select code from produce order by create_time desc limit 1")
    String getLastProduce();

    List<ProduceEntity> pageQryProduceForAdmin(@Param("orderCode") String orderCode, @Param("orderParam") String orderParam);
}
