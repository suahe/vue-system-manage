package com.example.manageSystem.admin.module.menu.controller;

import com.example.common.entity.response.Result;
import com.example.common.entity.response.StatusCode;
import com.example.manageSystem.admin.model.LogAnnotation;
import com.example.manageSystem.admin.model.Menu;
import com.example.manageSystem.admin.module.menu.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sys/menu")
@LogAnnotation(name="MenuController")
public class MenuController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    MenuService menuService;

    @GetMapping("/getMenuTreeByRoleId")
    @LogAnnotation(name="getMenuTreeByRoleId")
    public Result getMenuTreeByRoleId(Integer roleId){
        if (roleId==null){
            return Result.error(StatusCode.PARAMSERROR,"角色ID为空");
        }
        Map<String, Object> menuMap = menuService.getMenuTree(roleId);
        return Result.ok("查询成功",menuMap);
    }

    @GetMapping("/findAll")
    @LogAnnotation(name="findAll")
    public Result findAll(){
        List<Menu> menus = menuService.findAll();
        return Result.ok("查询成功",menus);
    }

    @GetMapping("findById")
    @LogAnnotation(name="findById")
    public Result findById(Integer id){
        Menu menu = menuService.findById(id);
        if(menu!=null&&menu.getId()!=null){
            return Result.ok("查询成功",menu);
        }else {
            return Result.error(StatusCode.SELECTERROR,"查询失败");
        }
    }

    @PostMapping("/add")
    @LogAnnotation(name="add")
    public Result add(@RequestBody Menu menu){
        if(menuService.add(menu)){
            return Result.ok("新增成功");
        }else {
            return Result.error(StatusCode.ADDERROR,"新增失败");
        }
    }

    @PostMapping("/edit")
    @LogAnnotation(name="edit")
    public Result edit(@RequestBody Menu menu){
        if (menu.getId()==menu.getParentId()){
            return  Result.error(StatusCode.PARAMSERROR,"上级资源不能为本身");
        }
        if(menuService.edit(menu)){
            return Result.ok("编辑成功");
        }else {
            return Result.error(StatusCode.EDITERROR,"编辑失败");
        }
    }

    @GetMapping("/del")
    public Result del(Integer id){
        if(menuService.del(id)){
            return Result.ok("删除成功");
        }else {
            return Result.error(StatusCode.DELETEERROR,"删除失败");
        }
    }
}
