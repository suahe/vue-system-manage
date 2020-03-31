package com.example.manageSystem.admin.module.config.controller;

import com.alibaba.fastjson.JSON;
import com.example.common.entity.response.Result;
import com.example.common.entity.response.StatusCode;
import com.example.manageSystem.admin.model.LogAnnotation;
import com.example.manageSystem.admin.module.config.service.ConfigService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/sys/config")
@RestController
@LogAnnotation(name="ConfigController")
public class ConfigController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ConfigService configService;

    @GetMapping( "/findAll" )
    @LogAnnotation(name="findAll")
    @RequiresPermissions("sys:config:findAll")
    public Map<String, String>  findAll(){
        return  configService.findAll();
    }

    @PostMapping("/saveConfig" )
    @LogAnnotation(name="saveConfig")
    @RequiresPermissions("sys:config:saveConfig")
    public Result saveConfig(@RequestBody HashMap<String,String> configMap){
        logger.info("调用ConfigController.saveConfig的参数："+ JSON.toJSONString(configMap));
        if(configService.saveConfig(configMap)){
            logger.info("保存成功");
            return Result.ok("保存成功");
        }else {
            logger.error("保存失败");
            return Result.error(StatusCode.ADDERROR,"保存失败");
        }
    }
}
