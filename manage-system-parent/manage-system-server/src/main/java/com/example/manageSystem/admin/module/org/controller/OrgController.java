package com.example.manageSystem.admin.module.org.controller;

import com.example.common.entity.response.Result;
import com.example.common.entity.response.StatusCode;
import com.example.manageSystem.admin.model.Org;
import com.example.manageSystem.admin.model.User;
import com.example.manageSystem.admin.module.org.service.OrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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


    @PostMapping("/addOrg")
    public Result addOrg(@RequestBody Org org){
        if(orgService.addOrg(org)){
            return Result.ok("添加成功");
        }else {
            return Result.error(StatusCode.ADDERROR,"添加失败");
        }
    }

    @PostMapping("/editOrg")
    public Result editOrg(@RequestBody Org org){
        if(orgService.editOrg(org)){
            return Result.ok("添加成功");
        }else {
            return Result.error(StatusCode.EDITERROR,"添加失败");
        }
    }

    @GetMapping("/del")
    public Result del(Integer orgId){
        //判断该机构是否存在用户
        List<User> users = orgService.getUsersByOrgId(orgId);
        if(users!=null&&users.size()>0){
            return Result.error(StatusCode.DELETEERROR,"该机构存在用户，请先将该机构用户删除");
        }
        //判断该机构是否存在下级
        List<Org> orgs = orgService.getOrgsByParentId(orgId);
        if(orgs!=null&&orgs.size()>0){
            return Result.error(StatusCode.DELETEERROR,"该机构存在子机构，请先将该机构子机构删除");
        }
        if(orgService.del(orgId)){
            return Result.ok("删除成功");
        }else {
            return Result.error(StatusCode.DELETEERROR,"删除失败");
        }
    }

    @GetMapping("/getOrgList")
    public Result getOrgList(){
        List<Org> orgs = orgService.getOrgList();
        return Result.ok("查询成功",orgs);
    }
 }
