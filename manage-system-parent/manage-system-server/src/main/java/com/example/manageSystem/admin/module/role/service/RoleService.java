package com.example.manageSystem.admin.module.role.service;

import com.example.manageSystem.admin.model.Role;
import com.example.manageSystem.admin.module.role.dao.RoleDao;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import java.util.Date;
import java.util.List;

@Service
public class RoleService {

    @Autowired
    RoleDao roleDao;

    public List<Role> findByUserId(Integer userId){
        return roleDao.findByUserId(userId);
    }

    public boolean add(Role role){
        role.setCreate_time(new Date());
        int i = roleDao.insert(role);
        return i>0;
    }

    public PageInfo<Role> findPage(String roleName, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex,pageSize);
        Example example = new Example(Role.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(roleName)){
            criteria.andEqualTo("role_name",roleName);
        }
        List<Role> roles = roleDao.selectByExample(example);
        PageInfo<Role> pageInfo = new PageInfo<Role>(roles);
        return pageInfo;
    }

    public Role findById(Integer id) {
        return roleDao.selectByPrimaryKey(id);
    }

    public boolean edit(Role role) {
        int i = 0;
        Role DbRole = this.findById(role.getRole_id());
        if (DbRole!=null){
            i = roleDao.updateByPrimaryKey(role);
        }
        return i>0;
    }

    public boolean del(Integer id) {
        int i = 0 ;
        Role role = findById(id);
        if (role!=null){
            i = roleDao.deleteByPrimaryKey(id);
        }
        return i>0;
    }

    //批量删除
    public boolean delByIds(Integer[] ids) {
        boolean bo = false;
        for (Integer id : ids) {
            bo = del(id);
        }
        return bo;
    }
}
