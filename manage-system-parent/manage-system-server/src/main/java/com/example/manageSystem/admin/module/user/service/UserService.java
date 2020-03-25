package com.example.manageSystem.admin.module.user.service;

import com.example.manageSystem.admin.model.User;
import com.example.manageSystem.admin.module.user.dao.UserDao;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

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

    public boolean addUser(User user) {
        User DbUser = this.findByUsername(user.getUsername());
        if (DbUser!=null){
            throw new RuntimeException("用户名称已存在");
        }
        int i = userDao.insert(user);
        return i>0;
    }

    public boolean editUser(User user) {
        User DbUser = this.findById(user.getUserId());
        if (DbUser==null){
            throw new RuntimeException("修改用户不存在");
        }
        DbUser = this.findByUsername(user.getUserId(), user.getUsername());
        if (DbUser!=null){
            throw new RuntimeException("用户名称已存在");
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

}
