package com.example.manageSystem.admin.module.config.controller;

import com.example.manageSystem.admin.model.Config;
import com.example.manageSystem.admin.module.config.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequestMapping("/sys/config")
@RestController
public class ConfigController {

    @Autowired
    ConfigService configService;

    @GetMapping(value = "/findPage" )
    public List<Config> findPage(){
        return  configService.findPage();
    }
}
