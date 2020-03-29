package com.example.manageSystem.admin.module.org.service;

import com.example.manageSystem.admin.model.Dict;
import com.example.manageSystem.admin.model.Org;
import com.example.manageSystem.admin.model.User;
import com.example.manageSystem.admin.module.dict.dao.DictDao;
import com.example.manageSystem.admin.module.org.dao.OrgDao;
import com.example.manageSystem.admin.module.user.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrgService {

    @Autowired
    OrgDao orgDao;
    @Autowired
    UserDao userDao;
    @Autowired
    DictDao dictDao;

    //生成组织树形结构
    public List<Org> getOrgTree(){
        List<Org> orgs = orgDao.selectAll();
        orgs = this.parseOrgTree(orgs);
        return orgs;
    }

    /**
     * @方法名: parseOrgTree<br>
     * @描述: 组装树状结构<br>
     * @param list 列表
     * @return
     */
    public  List<Org> parseOrgTree(List<Org> list){
        List<Org> menuTree = new ArrayList<Org>();

        // 1、获取第一级节点
        for (Org org : list) {
            if(null == org.getParentId()) {
                menuTree.add(org);
            }
        }

        // 2、递归获取子节点
        for (Org parent : menuTree) {
            parent = recursiveTree(parent, list);
        }

        return menuTree;
    }

    public  Org recursiveTree(Org parent, List<Org> list) {
        for (Org org : list) {
            if(parent.getOrgId() == org.getParentId()) {
                org = recursiveTree(org, list);
                parent.getChildren().add(org);
            }
        }
        return parent;
    }

    public Org findById(Integer orgId) {
        return orgDao.selectByPrimaryKey(orgId);
    }

    public Map<String, Object> getOrgAndUsersByOrgId(Integer orgId) {
        Map<String,Object> map = new HashMap<String, Object>();
        Org org = this.findById(orgId);
        String industryCategory = org.getIndustryCategory();
        List<Dict> industryCategorys = dictDao.getDictsByParentCode("industryCategory");
        for (Dict category : industryCategorys) {
            if(industryCategory.equals(category.getCode())){
                org.setIndustryCategory(category.getName());
            }
        }
        List<User> users = userDao.getUsersByOrgId(orgId,null);
        map.put("org",org);
        map.put("users",users);
        return map;
    }

    @Transactional
    public boolean addOrg(Org org) {
        Org DbOrg = findOrgByOrgName(org.getOrgName());
        if(DbOrg!=null){
            throw new RuntimeException("该组织机构名称已存在");
        }
        org.setOrgCode(createOrgCode());
        int i = orgDao.insert(org);
        return i>0;
    }

    @Transactional
    public boolean editOrg(Org org) {
        Org DbOrg = this.findById(org.getOrgId());
        if(DbOrg==null){
            throw new RuntimeException("该组织机构不存在");
        }
        DbOrg = findOrgByOrgName(org.getOrgId(),org.getOrgName());
        if(DbOrg!=null){
            throw new RuntimeException("该组织机构名称已存在");
        }
        int i = orgDao.updateByPrimaryKey(org);
        return i>0;
    }


    public Org findOrgByOrgName(String orgName){
        Example example = new Example(Org.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgName",orgName);
        return orgDao.selectOneByExample(example);
    }
    public Org findOrgByOrgName(Integer orgId , String orgName){
        Example example = new Example(Org.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgName",orgName);
        criteria.andNotEqualTo("orgId",orgId);
        return orgDao.selectOneByExample(example);
    }

    public boolean del(Integer orgId) {
        int i = orgDao.deleteByPrimaryKey(orgId);
        return i>0;
    }

    public List<User> getUsersByOrgId(Integer orgId) {
        return userDao.getUsersByOrgId(orgId,null);
    }

    public List<Org> getOrgList() {
        return orgDao.selectAll();
    }

    public String createOrgCode(){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateStr = format.format(new Date());
        String strCode = "SJ"+dateStr+(int)((Math.random()*9+1)*10000);
        return strCode;
    }

    //查询子组织机构
    public List<Org> getOrgsByParentId(Integer orgId) {
        Example example = new Example(Org.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("parentId",orgId);
        return orgDao.selectByExample(example);
    }
}
