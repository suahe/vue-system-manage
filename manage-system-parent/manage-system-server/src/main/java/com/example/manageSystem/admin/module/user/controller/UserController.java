package com.example.manageSystem.admin.module.user.controller;

import com.example.common.entity.response.PageResult;
import com.example.common.entity.response.Result;
import com.example.manageSystem.admin.model.User;
import com.example.manageSystem.admin.module.user.service.UserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/sys/user")
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/getUsersByOrgId")
    public PageResult<User> getUsersByOrgId(@RequestParam(value = "orgId",required = false) Integer orgId,
                                            @RequestParam(value = "username",required = false) String username,
                                            @RequestParam(value = "pageIndex",required = true, defaultValue = "1") int pageIndex,
                                            @RequestParam(value = "pageSize",required = true,defaultValue = "10") int pageSize){
        PageInfo<User> pageInfo = userService.getUsersByOrgId(orgId,username,pageIndex,pageSize);
        return  new PageResult<User>(pageInfo.getTotal(), pageInfo.getList());
    }

    @GetMapping("/findById")
    public Result findById(Integer userId){
        User user = userService.findById(userId);
        return Result.ok("查询成功",user);
    }

}
