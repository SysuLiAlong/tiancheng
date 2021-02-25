package com.tiancheng.ms.dao.mapper;

import com.tiancheng.ms.entity.UserEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<UserEntity> {

    @Select("select role_id from user_role_relation r join user u on u.id = r.user_id where u.user_name = #{userName}")
    String getRoleIdByUserName(@Param("userName") String userName);

    @Select("select * from user where user_name = #{userName} limit 1")
    UserEntity selectByUserName(@Param("userName") String userName);

    @Select("select * from user where token = #{token}")
    UserEntity selectUserByToken(@Param("token") String token);

    @Update("update user set password = #{newPassword} where id = #{userId}")
    void resetPasswd(Integer userId, String newPassword);

}
