package com.example.manageSystem.admin.module.role.service;

import com.example.manageSystem.admin.model.Menu;
import com.example.manageSystem.admin.model.Org;
import com.example.manageSystem.admin.model.Role;
import com.example.manageSystem.admin.module.menu.dao.MenuDao;
import com.example.manageSystem.admin.module.role.dao.RoleDao;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
public class RoleService {

    @Autowired
    RoleDao roleDao;
    @Autowired
    MenuDao menuDao;

    public List<Role> findByUserId(Integer userId){
        return roleDao.findByUserId(userId);
    }

    @Transactional
    public boolean add(Role role){
        //查询角色名称是否存在
        Role DbRole = this.findByRoleName(role.getRoleName());
        if (DbRole!=null){
            throw new RuntimeException("角色名称已存在");
        }
        role.setCreateTime(new Date());
        int i = roleDao.insert(role);
        return i>0;
    }

    public Role findByRoleName(String roleName){
        Example example = new Example(Role.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleName",roleName);
        return roleDao.selectOneByExample(example);
    }

    public Role findByRoleName(Integer roleId,String roleName){
        Example example = new Example(Role.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleName",roleName);
        criteria.andNotEqualTo("roleId",roleId);
        return roleDao.selectOneByExample(example);
    }

    public PageInfo<Role> findPage(String roleName, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex,pageSize);
        Example example = new Example(Role.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(roleName)){
            criteria.andEqualTo("roleName",roleName);
        }
        List<Role> roles = roleDao.selectByExample(example);
        PageInfo<Role> pageInfo = new PageInfo<Role>(roles);
        return pageInfo;
    }

    public Role findById(Integer id) {
        return roleDao.selectByPrimaryKey(id);
    }

    @Transactional
    public boolean edit(Role role) {
        Role DbRole = this.findById(role.getRoleId());
        if (DbRole==null){
            throw new RuntimeException("修改的角色不存在");
        }
        DbRole = this.findByRoleName(role.getRoleId(),role.getRoleName());
        if (DbRole!=null){
            throw new RuntimeException("角色名称已存在");
        }
        int i = roleDao.updateByPrimaryKey(role);
        return i>0;
    }

    @Transactional
    public boolean del(Integer id) {
        int i = 0 ;
        Role role = findById(id);
        if (role!=null){
            i = roleDao.deleteByPrimaryKey(id);
        }
        return i>0;
    }

    //批量删除
    @Transactional
    public boolean delByIds(Integer[] ids) {
        boolean bo = false;
        for (Integer id : ids) {
            bo = del(id);
        }
        return bo;
    }

    //保存角色分配的权限
    @Transactional
    public void saveRolePerms(Integer roleId, List<Integer> menuIdList) {
        //先查询该角色是否存在
        Role role = findById(roleId);
        if (role==null){
            throw new RuntimeException("角色不存在");
        }
        for (Integer id : menuIdList) {
            Menu menu = menuDao.selectByPrimaryKey(id);
            if (menu==null){
                throw new RuntimeException("添加的菜单不存在");
            }
        }
        //List<Integer> menuIdList = Arrays.asList(menuIds);
        roleDao.delPermsByRoleId(roleId);
        if (menuIdList!=null&&menuIdList.size()>0){
            roleDao.addPerms(roleId,menuIdList);
        }
    }

    public List<Role> findAll() {
        return roleDao.selectAll();
    }

    //根据用户id和组织id查询角色列表和已经分配给用户的角色名称列表
    public Map<String,Object> getRolesByOrgIdAndUserId(Integer orgId,Integer userId) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<Role> roles = this.getRolesByOrgId(orgId);
        List<Role> checkedRoleList = roleDao.findByUserId(userId);
        List<String> checkedRoles = new ArrayList<String>();
        for (Role role : checkedRoleList) {
            checkedRoles.add(role.getRoleName());
        }
        map.put("roles",roles);
        map.put("checkedRoles",checkedRoles);
        return map;
    }

    public void delByUserId(Integer userId) {
        roleDao.delByUserId(userId);
    }

    public List<Role> getByRoleNameList(List<String> roleNameList) {
        if (roleNameList==null&&roleNameList.size()==0){
            return new ArrayList<Role>();
        }
        List<Role> roles = roleDao.getByRoleNameList(roleNameList);
        return roles;
    }

    public List<Role> getRolesByOrgId(Integer orgId) {
        return roleDao.getRolesByOrgId(orgId);
    }
}
