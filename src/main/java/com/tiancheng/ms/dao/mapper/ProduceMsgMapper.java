package com.tiancheng.ms.dao.mapper;

import com.tiancheng.ms.entity.ProduceMsgEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ProduceMsgMapper extends Mapper<ProduceMsgEntity> {

    @Select("select * from produce_msg where produce_product_id = #{produceProductId} order by create_time")
    List<ProduceMsgEntity> listProduceMsg(@Param("produceProductId") Integer produceProductId);

    @Delete("delete from produce_msg where produce_id = #{produceId}")
    void deleteByProduceId(@Param("produceId") Integer produceId);
}
