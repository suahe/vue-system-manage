package com.example.manageSystem.admin.module.config.dao;

import com.example.manageSystem.admin.model.Config;
import tk.mybatis.mapper.common.Mapper;

public interface configDao extends Mapper<Config> {

    Config findById(String id);

}
