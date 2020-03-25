package com.example.manageSystem.admin.module.org.service;

import com.example.manageSystem.admin.model.Org;
import com.example.manageSystem.admin.model.User;
import com.example.manageSystem.admin.module.org.dao.OrgDao;
import com.example.manageSystem.admin.module.user.dao.UserDao;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrgService {
    @Autowired
    OrgDao orgDao;
    @Autowired
    UserDao userDao;

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

    public List<User> getUsersByOrgId(Integer orgId, String username, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex,pageSize);
        List<User> users = orgDao.getUsersByOrgId(orgId,username);
        return users;
    }
}
