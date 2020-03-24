package com.example.manageSystem.admin.module.menu.dao;

import com.example.manageSystem.admin.model.Menu;
import tk.mybatis.mapper.common.Mapper;
import java.util.List;

public interface MenuDao extends Mapper<Menu> {

    List<Menu> findByRoleId(Integer roleId);
}
