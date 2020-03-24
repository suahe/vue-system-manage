package com.example.manageSystem.admin.module.config.service;

import com.example.manageSystem.admin.model.Config;
import com.example.manageSystem.admin.module.config.dao.configDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class ConfigService {

    @Autowired
    configDao configDao;

    public List<Config> findPage() {
        List<Config> sysConfigs = configDao.selectAll();
        return sysConfigs;
    }
}
