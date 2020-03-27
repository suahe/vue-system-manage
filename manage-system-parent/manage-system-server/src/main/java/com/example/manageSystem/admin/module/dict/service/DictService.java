package com.example.manageSystem.admin.module.dict.service;

import com.example.manageSystem.admin.model.Dict;
import com.example.manageSystem.admin.module.dict.dao.DictDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class DictService {

    @Autowired
    DictDao dictDao;


    public List<Dict> getDictsByParentCode(String parentCode) {
        List<Dict> dicts = dictDao.getDictsByParentCode(parentCode);
        return dicts;
    }
}
