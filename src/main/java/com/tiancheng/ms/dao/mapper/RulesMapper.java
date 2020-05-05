package com.tiancheng.ms.dao.mapper;

import com.tiancheng.ms.entity.RulesEntity;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;

public interface RulesMapper extends Mapper<RulesEntity>, MySqlMapper<RulesEntity> {

    List<RulesEntity> selectByProductId(@Param("productId") Integer productId);

}
