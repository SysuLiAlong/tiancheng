package com.tiancheng.ms.dao.mapper;

import com.tiancheng.ms.dto.ProduceDetailDTO;
import com.tiancheng.ms.entity.ProduceEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface ProduceMapper extends Mapper<ProduceEntity> {
    @Select("select produce_code from produce order by create_time desc limit 1")
    String getLastProduce();

    ProduceDetailDTO getProduceDetail(@Param("produceId") Integer produceId);
}
