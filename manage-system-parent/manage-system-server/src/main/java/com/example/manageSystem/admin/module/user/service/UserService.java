package com.example.manageSystem.admin.module.user.service;

import com.example.manageSystem.admin.model.User;
import com.example.manageSystem.admin.module.user.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    //根据用户名查询
    public User findByUserName(String username){
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username",username);
        return  userDao.selectOneByExample(example);
    }
}
