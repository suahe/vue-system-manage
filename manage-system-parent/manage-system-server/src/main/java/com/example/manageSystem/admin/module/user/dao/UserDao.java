package com.example.manageSystem.admin.module.user.dao;

import com.example.manageSystem.admin.model.User;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserDao  extends Mapper<User> {

    List<User> getUsersByOrgId(@Param("orgId") Integer orgId, @Param("username") String username);
}
