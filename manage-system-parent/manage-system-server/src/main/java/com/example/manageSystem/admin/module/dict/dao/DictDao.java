package com.example.manageSystem.admin.module.dict.dao;

import com.example.manageSystem.admin.model.Dict;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface DictDao extends Mapper<Dict> {

    List<Dict> getDictsByParentCode(String parentCode);
}
