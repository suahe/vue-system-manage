package com.example.manageSystem.admin.module.dict.controller;

import com.example.common.entity.response.PageResult;
import com.example.common.entity.response.Result;
import com.example.common.entity.response.StatusCode;
import com.example.manageSystem.admin.model.Dict;
import com.example.manageSystem.admin.module.dict.service.DictService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/sys/dict")
@RestController
public class DictController {

    @Autowired
    private DictService dictService;

    @GetMapping("/findPage")
    public PageResult<Dict> findPage(@RequestParam(value ="parentId" ,required = false) Integer parentId ,
                           @RequestParam(value = "name" ,required = false) String name ,
                           @RequestParam(value ="code" ,required = false) String code ,
                           @RequestParam(value = "pageIndex",required = true, defaultValue = "1") int pageIndex,
                           @RequestParam(value = "pageSize",required = true,defaultValue = "10") int pageSize){
        PageInfo<Dict> pageInfo = dictService.findPage(parentId,name,code,pageIndex,pageSize);
        return new PageResult<Dict>(pageInfo.getTotal(),pageInfo.getList());
    }

    @RequestMapping("/getDictsByParentCode")
    public Result getDictsByParentCode(String parentCode){
        List<Dict> dicts = dictService.getDictsByParentCode(parentCode);
        return Result.ok("查询成功",dicts);
    }

    @GetMapping("/getDicts")
    public Result getDicts(){
        List<Dict> dicts = dictService.getDicts();
        return Result.ok("查询成功",dicts);
    }

    @GetMapping("/getDictsByParentId")
    public Result getDictsByParentId(Integer parentId){
        List<Dict> dicts = dictService.getDictsByParentId(parentId);
        return Result.ok("查询成功",dicts);
    }

    @GetMapping("/findById")
    public Result findById(Integer id){
        Dict dict = dictService.findById(id);
        return Result.ok("查询成功",dict);
    }

    @PostMapping("/add")
    public Result add(@RequestBody Dict dict){
        if(dictService.add(dict)){
            return Result.ok("新增成功");
        }else {
            return Result.error(StatusCode.ADDERROR,"新增失败");
        }
    }

    @PostMapping("/edit")
    public Result edit(@RequestBody Dict dict){
        if(dictService.edit(dict)){
            return Result.ok("编辑成功");
        }else {
            return Result.error(StatusCode.EDITERROR,"编辑失败");
        }
    }

    @GetMapping("/del")
    public Result del(Integer id){
        if(dictService.del(id)){
            return Result.ok("删除成功");
        }else {
            return Result.error(StatusCode.DELETEERROR,"删除失败");
        }
    }

    @GetMapping("/delDictsByIds")
    public Result delDictsByIds(Integer[] ids){
        if(dictService.delDictsByIds(ids)){
            return Result.ok("删除成功");
        }else {
            return Result.error(StatusCode.DELETEERROR,"删除失败");
        }
    }
}
