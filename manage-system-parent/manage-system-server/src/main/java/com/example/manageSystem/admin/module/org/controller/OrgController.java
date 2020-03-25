package com.example.manageSystem.admin.module.org.controller;

import com.example.common.entity.response.Result;
import com.example.manageSystem.admin.model.Org;
import com.example.manageSystem.admin.model.User;
import com.example.manageSystem.admin.module.org.service.OrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

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

    @GetMapping("/getUsersByOrgId")
    public Result getUsersByOrgId( @RequestParam(value = "orgId",required = false) Integer orgId,
                                   @RequestParam(value = "username",required = false) String username,
                                   @RequestParam(value = "pageIndex",required = true, defaultValue = "1") int pageIndex,
                                   @RequestParam(value = "pageSize",required = true,defaultValue = "10") int pageSize){
        List<User> users= orgService.getUsersByOrgId(orgId,username,pageIndex,pageSize);
        return  Result.ok("查询成功",users);
    }
}
