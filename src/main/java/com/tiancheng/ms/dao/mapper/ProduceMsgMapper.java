package com.tiancheng.ms.dao.mapper;

import com.tiancheng.ms.entity.ProduceMsgEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ProduceMsgMapper extends Mapper<ProduceMsgEntity> {

    @Select("select * from produce_msg where produce_id = #{produceId} order by create_time")
    List<ProduceMsgEntity> listProduceMsg(@Param("produceId") Integer produceId);
}
