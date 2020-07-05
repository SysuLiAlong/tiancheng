package com.tiancheng.ms.dao.mapper;

import com.tiancheng.ms.entity.ProcessEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ProcessMapper extends Mapper<ProcessEntity> {

    List<ProcessEntity> selectByProductId(@Param("productId") Integer productId);

    List<ProcessEntity> getProduceProcess(@Param("productCode") String productCode);

    @Select("select * from process where type = 1 order by priority")
    List<ProcessEntity> listPriorityProcess();

    @Select("select * from process where name = #{name} limit 1")
    ProcessEntity selectOneByName(@Param("name") String name);

    @Select("select max(priority) from process")
    Integer selectMaxPriority();
}
