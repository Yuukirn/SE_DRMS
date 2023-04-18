package com.nqff.drms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nqff.drms.dao.UserDao;
import com.nqff.drms.pojo.User;
import com.nqff.drms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public boolean isUserExisted(String email) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(email != null, User::getEmail, email);
        List<User> users = userDao.selectList(wrapper);
        return users != null && users.size() != 0;
    }

    @Override
    public User selectUserByEmail(String email) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(email != null, User::getEmail, email);
        List<User> users = userDao.selectList(wrapper);
        if (users == null || users.size() == 0) {
            return null;
        }
        return users.get(0);
    }

    @Override
    public void insertUser(User user) {
        userDao.insert(user);
    }
}
