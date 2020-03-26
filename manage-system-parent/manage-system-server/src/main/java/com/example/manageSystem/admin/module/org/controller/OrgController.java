package com.example.manageSystem.admin.module.org.controller;

import com.example.common.entity.response.Result;
import com.example.manageSystem.admin.model.Org;
import com.example.manageSystem.admin.module.org.service.OrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RequestMapping("/sys/org")
@RestController
public class OrgController {

    @Autowired
    OrgService orgService;

    @GetMapping("/getOrgTree")
    public Result getOrgTree(){
        List<Org> orgTree = orgService.getOrgTree();
        return Result.ok("查询成功",orgTree);
    }

    @GetMapping("/findById")
    public Result findById(Integer orgId){
        Org org = orgService.findById(orgId);
        return Result.ok("查询成功",org);
    }

    @GetMapping("/getOrgAndUsersByOrgId")
    public Result getOrgAndUsersByOrgId(Integer orgId){
        Map<String,Object> map = orgService.getOrgAndUsersByOrgId(orgId);
        return Result.ok("查询成功",map);
    }
 }
