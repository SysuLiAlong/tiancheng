package com.tiancheng.ms.dao.mapper;

import com.tiancheng.ms.entity.UserEntity;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserMapper extends Mapper<UserEntity> {

    List<UserEntity> queryByRoleId(Integer roleId);
}
