package com.tiancheng.ms.dao.mapper;

import com.tiancheng.ms.dto.ProduceProcessDTO;
import com.tiancheng.ms.entity.ProcessEntity;
import com.tiancheng.ms.entity.ProduceProcessEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;

public interface ProduceProcessMapper extends Mapper<ProduceProcessEntity> , MySqlMapper<ProduceProcessEntity> {

    List<ProduceProcessDTO> listProduceProcess(@Param("produceProductId") Integer produceProductId);

    ProduceProcessEntity getNextProcess(@Param("produceProcessId") Integer produceProcessId);

    @Select("select * from produce_process where status in (1,2) and produce_product_id = #{produceProductId} limit 1")
    ProduceProcessEntity getCurrentProcess(@Param("produceProductId") Integer produceProductId);

    @Delete("delete from produce_process where produce_id = #{produceId}")
    void deleteByProduceId(@Param("produceId") Integer produceId);

    @Select("select p2.* from produce_process p1 left join process p2 on p1.process_id = p2.id" +
            " where p1.produce_product_id = #{produceProductId} and p1.status in (1,2) limit 1")
    ProcessEntity selectCurrentProcess(@Param("produceProductId") Integer produceProductId);

    @Select("select * from produce_process where produce_product_id = #{produceProductId} and next_process = #{currentProcessId}")
    ProduceProcessEntity getLastProcess(@Param("produceProductId") Integer produceProductId,@Param("currentProcessId") Integer currentProcessId);

    @Update("update produce_process set next_process = #{newProcessId} where next_process = #{oldProcessId}")
    void changeNextProcess(@Param("oldProcessId") Integer first,@Param("newProcessId") Integer second);

    List<ProduceProcessDTO> selectUnOverProduceContainProcessId(@Param("processId") Integer processId,@Param("endProcessId") Integer endProcesssId);

    @Select("select * from produce_process where produce_product_id = #{produceProductId} and process_Id = #{endProcessId}")
    ProduceProcessEntity selectEndProduceProcess(@Param("produceProductId") Integer produceProductId,@Param("endProcessId") Integer endProcessId);

    @Select("select * from produce_process where produce_product_id = #{produceProductId} and process_id = #{startProcessId}")
    ProduceProcessEntity selectStartProduceProcess(@Param("produceProductId") Integer produceProductId,@Param("startProcessId") Integer startProcessId);

    // 流程负责人不是admin这种系统用户，说明produce还未结束
    @Select("select produce_id from produce_process where charge_user_name = #{chargeUserName} and status in (1,2)")
    List<Integer> queryUnOverProduceIdsByChargeUserName(@Param("chargeUserName") String chargeUserName);

    @Select("select * from produce_process where produce_id = #{produceId} and status in (1,2)")
    List<ProduceProcessEntity> queryUnOverProduceProcessByProduceId(@Param("produceId") Integer produceId);
}
