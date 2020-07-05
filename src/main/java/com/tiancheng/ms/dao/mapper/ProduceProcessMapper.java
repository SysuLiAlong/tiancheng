package com.tiancheng.ms.dao.mapper;

import com.tiancheng.ms.dto.ProduceProcessDTO;
import com.tiancheng.ms.entity.ProcessEntity;
import com.tiancheng.ms.entity.ProduceProcessEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;

public interface ProduceProcessMapper extends Mapper<ProduceProcessEntity> , MySqlMapper<ProduceProcessEntity> {

    List<ProduceProcessDTO> listProduceProcess(@Param("produceId") Integer produceId);

    ProduceProcessEntity getNextProcess(@Param("produceProcessId") Integer produceProcessId);

    @Select("select * from produce_process where status in (1,2) and produce_id = #{produceId} limit 1")
    ProduceProcessEntity getCurrentProcess(@Param("produceId") Integer produceId);

    @Delete("delete from produce_process where produce_id = #{produceId}")
    void deleteByProduceId(@Param("produceId") Integer produceId);

    @Select("select p2.* from produce_process p1 left join process p2 on p1.process_id = p2.id" +
            " where p1.produce_id = #{produceId} and p1.status in (1,2) limit 1")
    ProcessEntity selectCurrentProcess(@Param("produceId") Integer produceId);

    @Select("select * from produce_process where produce_id = #{produceId} and next_process = #{currentProcessId}")
    ProduceProcessEntity getLastProcess(@Param("produceId") Integer produceId,@Param("currentProcessId") Integer currentProcessId);
}
