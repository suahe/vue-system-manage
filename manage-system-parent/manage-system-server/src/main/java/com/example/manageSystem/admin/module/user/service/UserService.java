package com.example.manageSystem.admin.module.user.service;

import com.example.manageSystem.admin.model.Role;
import com.example.manageSystem.admin.model.User;
import com.example.manageSystem.admin.module.role.service.RoleService;
import com.example.manageSystem.admin.module.user.dao.UserDao;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    UserDao userDao;
    @Autowired
    RoleService roleService;
    //根据用户名查询
    public User findByUsername(String username){
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username",username);
        return  userDao.selectOneByExample(example);
    }

    public PageInfo<User> getUsersByOrgId(Integer orgId, String username, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex,pageSize);
        List<User> users = userDao.getUsersByOrgId(orgId,username);
        PageInfo<User> pageInfo = new PageInfo<User>(users);
        return pageInfo;
    }

    public User findById(Integer userId) {
        User user = userDao.selectByPrimaryKey(userId);
        return user;
    }

    @Transactional
    public boolean addUser(User user) {
        User DbUser = this.findByUsername(user.getUsername());
        if (DbUser!=null){
            throw new RuntimeException("用户名称已存在");
        }
        user.setStatus((short)1);//0：禁用 1：正常
        user.setCreateTime(new Date());
        int i = userDao.insert(user);
        //再插入角色和用户的中间表
        int y = 0;
        if(user.getCheckedRoles()!=null&&user.getCheckedRoles().size()>0){
            List<Role> checkedRoles = roleService.getByRoleNameList(user.getCheckedRoles());
            //插入中间表
            List<Integer> roleIds = new ArrayList<Integer>();
            for (Role role : checkedRoles) {
                roleIds.add(role.getRoleId());
            }
            y = userDao.addUserRoles(user.getUserId(), roleIds);
        }

        return i+y>0;
    }

    @Transactional
    public boolean editUser(User user) {
        User DbUser = this.findById(user.getUserId());
        if (DbUser==null){
            throw new RuntimeException("修改用户不存在");
        }
        DbUser = this.findByUsername(user.getUserId(), user.getUsername());
        if (DbUser!=null){
            throw new RuntimeException("用户名称已存在");
        }
        //删除用户的角色
        roleService.delByUserId(user.getUserId());
        //再添加勾选的角色列表（根据角色名称列表，查询角色列表）
        if(user.getCheckedRoles()!=null&&user.getCheckedRoles().size()>0){
            List<Role> checkedRoles = roleService.getByRoleNameList(user.getCheckedRoles());
            //插入中间表
            List<Integer> roleIds = new ArrayList<Integer>();
            for (Role role : checkedRoles) {
                roleIds.add(role.getRoleId());
            }
            userDao.addUserRoles(user.getUserId(),roleIds);
        }

        int i = userDao.updateByPrimaryKey(user);
        return i>0;
    }

    //查询用户名称，但不等于自身id
    public User findByUsername(Integer userId, String username){
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username",username);
        criteria.andNotEqualTo("userId",userId);
        return userDao.selectOneByExample(example);
    }

    public Map<String, Object> getUserAndRolesById(Integer userId, Integer orgId) {
        User user = findById(userId);
        if(user ==null){
            throw new RuntimeException("该用户不存在");
        }
        Map<String, Object> map = roleService.getRolesByOrgIdAndUserId(orgId, userId);
        map.put("user",user);
        return map;
    }

    @Transactional
    public boolean del(Integer userId) {
        int i = userDao.deleteByPrimaryKey(userId);
        int x = delUserAndRoleByUserId(userId);
        int y = delUserAndRoleByUserId(userId);
        return i+x+y>0;
    }

    //删除用户和角色的中间表
    public int delUserAndRoleByUserId(Integer userId) {
        return userDao.delUserAndRoleByUserId(userId);
    }
    //删除用户和组织的中间表
    public int delUserAndOrgByUserId(Integer userId){
        return userDao.delUserAndOrgByUserId(userId);
    }
}
