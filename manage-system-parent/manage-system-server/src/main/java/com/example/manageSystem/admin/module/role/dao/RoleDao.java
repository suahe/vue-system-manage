package com.example.manageSystem.admin.module.role.dao;

import com.example.manageSystem.admin.model.Role;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface RoleDao extends Mapper<Role> {

    List<Role> findByUserId(Integer userId);

}
