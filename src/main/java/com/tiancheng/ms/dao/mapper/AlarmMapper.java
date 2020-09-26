package com.tiancheng.ms.dao.mapper;

import com.tiancheng.ms.dto.AlarmDTO;
import com.tiancheng.ms.entity.AlarmEntity;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface AlarmMapper extends Mapper<AlarmEntity> {

    List<AlarmDTO> listAlarm(@Param("currentUser") String currentUser, @Param("type") Integer type);
}
