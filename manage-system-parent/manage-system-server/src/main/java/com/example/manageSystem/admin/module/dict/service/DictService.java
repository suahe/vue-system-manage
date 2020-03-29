package com.example.manageSystem.admin.module.dict.service;

import com.example.manageSystem.admin.model.Dict;
import com.example.manageSystem.admin.module.dict.dao.DictDao;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class DictService {

    @Autowired
    DictDao dictDao;
    private List<Dict> dicts;


    public List<Dict> getDictsByParentCode(String parentCode) {
      List<Dict> dicts = dictDao.getDictsByParentCode(parentCode);

        return dicts;
    }

    public List<Dict> getDicts() {
        List<Dict> dicts = dictDao.selectAll();
        return this.dicts;
    }

    public List<Dict> getDictsByParentId(Integer parentId) {
        List<Dict> dicts = dictDao.getDictsByParentId(parentId);
        return dicts;
    }

    public Dict findById(Integer id) {
        return  dictDao.selectByPrimaryKey(id);
    }

    @Transactional
    public boolean add(Dict dict) {
        //查询有没有相同的名称和编码
        Dict DbDict = dictDao.findDictByParentAndCode(dict.getParentId(), dict.getCode());
        if (DbDict!=null){
            throw new RuntimeException("已存在相同的字典编码");
        }
        int i = dictDao.insert(dict);
        return i>0;
    }

    @Transactional
    public boolean edit(Dict dict) {
        Dict DbDict = this.findById(dict.getId());
        if (DbDict==null){
            throw new RuntimeException("不存在该字典信息");
        }
        //查询有没有相同的名称和编码
        DbDict = dictDao.findDictByParentIdAndCode(dict.getId(),dict.getParentId(), dict.getCode());
        if (DbDict!=null){
            throw new RuntimeException("已存在相同的字典编码");
        }
        int i = dictDao.updateByPrimaryKey(dict);
        return i>0;
    }

    @Transactional
    public boolean del(Integer id){
        Dict DbDict = this.findById(id);
        if(DbDict==null){
            throw new RuntimeException("不存在该字典信息");
        }
        int i = dictDao.deleteByPrimaryKey(id);
        return i>0;
    }

    public boolean delDictsByIds(Integer[] ids) {
        boolean bo = false;
        for (Integer id : ids) {
            bo =  this.del(id);
        }
        return bo;
    }

    public PageInfo<Dict> findPage(Integer parentId, String name, String code, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex,pageSize);
        List<Dict> dicts = dictDao.findPage(parentId,name,code);
        PageInfo<Dict> pageInfo = new PageInfo<>(dicts);
        return pageInfo;
    }
}
