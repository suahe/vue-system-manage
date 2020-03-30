package com.example.manageSystem.admin.module.user.controller;

import com.example.common.entity.response.PageResult;
import com.example.common.entity.response.Result;
import com.example.common.entity.response.StatusCode;
import com.example.manageSystem.admin.model.LogAnnotation;
import com.example.manageSystem.admin.model.Role;
import com.example.manageSystem.admin.model.User;
import com.example.manageSystem.admin.module.user.service.UserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/sys/user")
@RestController
@LogAnnotation(name="UserController")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/getUsersByOrgId")
    @LogAnnotation(name="getUsersByOrgId")
    public PageResult<User> getUsersByOrgId(@RequestParam(value = "orgId",required = false) Integer orgId,
                                            @RequestParam(value = "username",required = false) String username,
                                            @RequestParam(value = "pageIndex",required = true, defaultValue = "1") int pageIndex,
                                            @RequestParam(value = "pageSize",required = true,defaultValue = "10") int pageSize){
        PageInfo<User> pageInfo = userService.getUsersByOrgId(orgId,username,pageIndex,pageSize);
        return  new PageResult<User>(pageInfo.getTotal(), pageInfo.getList());
    }

    @GetMapping("/findById")
    @LogAnnotation(name="findById")
    public Result findById(Integer userId){
        User user = userService.findById(userId);
        return Result.ok("查询成功",user);
    }

    @GetMapping("/getUserAndRolesById")
    @LogAnnotation(name="getUserAndRolesById")
    public Result getUserAndRolesById(@RequestParam("userId") Integer userId,@RequestParam("orgId") Integer orgId){
        Map<String,Object> map = userService.getUserAndRolesById(userId,orgId);
        return Result.ok("查询成功",map);
    }

    @PostMapping("/addUser")
    @LogAnnotation(name="addUser")
    public Result addUser(@RequestBody User user){
        if(userService.addUser(user)){
            return Result.ok("添加成功",user);
        }else{
            return Result.error(StatusCode.ADDERROR,"添加失败");
        }
    }

    @PostMapping("/editUser")
    @LogAnnotation(name="editUser")
    public Result editUser(@RequestBody User user){
        if(userService.editUser(user)){
            return Result.ok("编辑成功",user);
        }else{
            return Result.error(StatusCode.EDITERROR,"编辑成功");
        }
    }

    @GetMapping("/del")
    @LogAnnotation(name="del")
    public Result del(Integer userId){
        if(userService.del(userId)){
            return Result.ok("删除成功");
        }else{
            return Result.error(StatusCode.DELETEERROR,"删除失败");
        }
    }

    @GetMapping("/delUserByIds")
    @LogAnnotation(name="delUserByIds")
    public Result delUserByIds(Integer[] ids){
        if(userService.delUserByIds(ids)){
            return Result.ok("删除成功");
        }else{
            return Result.error(StatusCode.DELETEERROR,"删除失败");
        }
    }

    @GetMapping("/resetPassword")
    @LogAnnotation(name="resetPassword")
    public Result resetPassword(Integer userId){
        if(userService.resetPassword(userId)){
            return Result.ok("重置成功");
        }else{
            return Result.error(StatusCode.RESETERROR,"重置失败");
        }
    }
}
