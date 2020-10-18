package com.tiancheng.ms.dao.mapper;

import com.tiancheng.ms.dto.OverTimeAlarmDTO;
import com.tiancheng.ms.entity.RulesEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;

public interface RulesMapper extends Mapper<RulesEntity>, MySqlMapper<RulesEntity> {

    List<RulesEntity> selectByProductId(@Param("productId") Integer productId);

    @Delete("delete from rules where product_id = #{productId}")
    void deleteByProductId(@Param("productId") Integer productId);

    List<OverTimeAlarmDTO> queryOverTimeAlarmDTO();

    @Delete("delete from alarm where type = 1")
    void deleteOverTimeAlarm();
}
