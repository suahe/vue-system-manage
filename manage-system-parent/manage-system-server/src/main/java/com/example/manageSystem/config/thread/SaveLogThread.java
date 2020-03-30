package com.example.manageSystem.config.thread;

import com.example.manageSystem.admin.model.Log;
import com.example.manageSystem.admin.module.log.service.LogService;

/**
 * 日志线程
 */
public class SaveLogThread implements Runnable{

    private Log log;
    private LogService logService;

    public SaveLogThread(Log log, LogService logService) {
        this.log = log;
        this.logService = logService;
    }
    @Override
    public void run() {
        logService.save(log);
    }
}

