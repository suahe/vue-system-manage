package com.example.manageSystem.admin.module.role.controller;

import com.alibaba.fastjson.JSON;
import com.example.common.entity.response.PageResult;
import com.example.common.entity.response.Result;
import com.example.common.entity.response.StatusCode;
import com.example.manageSystem.admin.model.LogAnnotation;
import com.example.manageSystem.admin.model.Role;
import com.example.manageSystem.admin.module.role.service.RoleService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sys/role")
@LogAnnotation(name="RoleController")
public class RoleController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    RoleService roleService;

    @GetMapping("/findPage")
    @LogAnnotation(name="findPage")
    public PageResult<Role> findPage(
            @RequestParam(value = "role_name",required = false) String roleName,
            @RequestParam(value = "pageIndex",required = true, defaultValue = "1") int pageIndex,
            @RequestParam(value = "pageSize",required = true,defaultValue = "10") int pageSize){
        PageInfo<Role> pageInfo = roleService.findPage(roleName, pageIndex, pageSize);
        return new PageResult<Role>(pageInfo.getTotal(),pageInfo.getList());
    }

    @PostMapping("/add")
    @LogAnnotation(name="add")
    public Result add(@RequestBody Role role){
        if (roleService.add(role)) {
            return Result.ok("新增成功");
        }else {
            return Result.error( StatusCode.ADDERROR,"新增失败");
        }
    }

    @GetMapping("/findById")
    @LogAnnotation(name="findById")
    public Result findByUserId(Integer id){
        Role role = roleService.findById(id);
        if (role==null){
            return Result.error(StatusCode.SELECTERROR,"查询失败");
        }else {
            return Result.ok("查询成功",role);
        }
    }

    @PostMapping("/edit")
    @LogAnnotation(name="edit")
    public Result edit(@RequestBody Role role){
        if (role==null||role.getRoleId() == null){
            return Result.error(StatusCode.PARAMSERROR,"参数错误");
        }
        if (roleService.edit(role)) {
            return Result.ok("编辑成功");
        }else {
            return Result.error( StatusCode.EDITERROR,"编辑失败");
        }
    }

    @GetMapping("/del")
    @LogAnnotation(name="del")
    public Result del(Integer id){
        if (id == null){
            return Result.error(StatusCode.PARAMSERROR,"参数错误");
        }
        if (roleService.del(id)) {
            return Result.ok("删除成功");
        }else {
            return Result.error( StatusCode.DELETEERROR,"删除失败");
        }
    }

    @GetMapping("/delByIds")
    @LogAnnotation(name="delByIds")
    public Result delByIds(Integer[] ids){
        if (ids == null|| ids.length==0){
            return Result.error( StatusCode.PARAMSERROR,"参数错误");
        }
        if (roleService.delByIds(ids)) {
            return Result.ok("删除成功");
        }else {
            return Result.error(StatusCode.DELETEERROR,"删除失败");
        }
    }

    @PostMapping("/saveRolePerms")
    @LogAnnotation(name="saveRolePerms")
    public Result saveRolePerms(@RequestBody HashMap params){
        logger.info("MenuController.saveRolePerms参数："+ JSON.toJSONString(params));
        Integer roleId = null;
        List<Integer> menuIdList = new ArrayList<Integer>();
        if(params.get("roleId")!=null){
            roleId = (Integer) params.get("roleId");
        }
        if(params.get("menuIds")!=null){
            menuIdList = (List<Integer>) params.get("menuIds");
        }
        roleService.saveRolePerms(roleId,menuIdList);
        return Result.ok("添加权限成功");
    }

    //只能添加选择机构对应及以下的角色权限
    @GetMapping("/getRolesByOrgIdAndUserId")
    @LogAnnotation(name="getRolesByOrgIdAndUserId")
    public Result getRolesByOrgIdAndUserId(Integer orgId,Integer userId){
        if(orgId==null){
            return Result.error("请选择组织机构");
        }
        //先查询所有的角色
        Map<String,Object> map = roleService.getRolesByOrgIdAndUserId(orgId,userId);
        return Result.ok("查询角色成功",map);
    }


    @GetMapping("/getRolesByOrgId")
    @LogAnnotation(name="getRolesByOrgId")
    public Result getRolesByOrgId(Integer orgId){
        List<Role> roles = roleService.getRolesByOrgId(orgId);
        return Result.ok("查询成功",roles);
    }
}
