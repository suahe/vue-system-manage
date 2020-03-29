package com.example.manageSystem.admin.module.log.service;

import com.example.manageSystem.admin.model.Log;
import com.example.manageSystem.admin.module.log.dao.LogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogService {

    @Autowired
    LogDao logDao;

    public boolean save(Log log){
       int i =  logDao.insert(log);
       return i>0;
    }
}
