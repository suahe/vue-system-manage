package com.example.manageSystem.admin.module.org.dao;

import com.example.manageSystem.admin.model.Org;
import com.example.manageSystem.admin.model.User;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OrgDao extends Mapper<Org> {

    List<User> getUsersByOrgId(@Param("orgId") Integer orgId,@Param("username") String username);
}
