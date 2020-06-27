package com.tiancheng.ms.dao.mapper;

import com.tiancheng.ms.dto.ProduceProcessDTO;
import com.tiancheng.ms.entity.ProduceProcessEntity;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;

public interface ProduceProcessMapper extends Mapper<ProduceProcessEntity> , MySqlMapper<ProduceProcessEntity> {

    List<ProduceProcessDTO> listProduceProcess(@Param("produceId") Integer produceId);
}
