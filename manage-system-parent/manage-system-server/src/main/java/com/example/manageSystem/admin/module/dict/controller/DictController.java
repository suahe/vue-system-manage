package com.example.manageSystem.admin.module.dict.controller;

import com.example.common.entity.response.Result;
import com.example.manageSystem.admin.model.Dict;
import com.example.manageSystem.admin.module.dict.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RequestMapping("/sys/dict")
@RestController
public class DictController {

    @Autowired
    private DictService dictService;

    @RequestMapping("/getDictsByParentCode")
    public Result getDictsByParentCode(String parentCode){
        List<Dict> dicts = dictService.getDictsByParentCode(parentCode);
        return Result.ok("查询成功",dicts);
    }

}
