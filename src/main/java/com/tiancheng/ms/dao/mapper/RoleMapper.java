package com.tiancheng.ms.dao.mapper;

import com.tiancheng.ms.entity.RoleEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface RoleMapper extends Mapper<RoleEntity> {
    @Select("select role.* from role inner join user_role_relation relation " +
            "on relation.role_id = role.id and relation.user_id = #{userId} limit 1")
    @ResultMap("BaseResultMap")
    RoleEntity queryRoleOfUser(@Param("userId") Integer userId);
}
