package com.example.manageSystem.admin.module.config.service;

import com.example.manageSystem.admin.model.Config;
import com.example.manageSystem.admin.module.config.dao.configDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Service
public class ConfigService {

    @Autowired
    configDao configDao;

    public Map<String, String> findAll() {
        Example example = new Example(Config.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status",1);
        List<Config> sysConfigs = configDao.selectByExample(example);
        Map<String, String> map = new HashMap<>();
        for (Config sysConfig : sysConfigs) {
            map.put(sysConfig.getParamKey(),sysConfig.getParamValue());
        }
        return map;
    }

    @Transactional
    public boolean saveConfig(HashMap<String, String> configMap) {
        //获取所有的键
        boolean bo = false;
        Set<String> keys = configMap.keySet();
        for (String key : keys) {
            Config config = findConfigByKey(key);
            if(config==null){
                //新增
                bo  = save(key, configMap.get(key));
            }else {
                //修改
                config.setParamValue(configMap.get(key));
                int i  = configDao.updateByPrimaryKey(config);
                bo = i > 0;
            }
        }
        return bo;
    }

    public boolean save(String key ,String value){
        Config config = new Config();
        config.setParamKey(key);
        config.setParamValue(value);
        config.setStatus((short)1);//显示
        int i = configDao.insert(config);
        return i>0;
    }


    public Config findConfigByKey(String key){
        Example example = new Example(Config.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("paramKey",key);
        return configDao.selectOneByExample(example);
    }
}
