package com.example.manageSystem.admin.module.role.dao;

import com.example.manageSystem.admin.model.Role;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface RoleDao extends Mapper<Role> {

    List<Role> findByUserId(Integer userId);

    int addPerms(@Param("roleId") Integer roleId,@Param("menuIdList") List<Integer> menuIdList);

    int delPermsByRoleId(Integer roleId);

    int delByUserId(Integer userId);

    List<Role> getByRoleNameList(List<String> roleNameList);
}
