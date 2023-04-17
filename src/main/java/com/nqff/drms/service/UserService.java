package com.nqff.drms.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nqff.drms.dao.UserDao;
import com.nqff.drms.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public boolean isUserExisted(String email) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(email != null, User::getEmail, email);
        List<User> users = userDao.selectList(wrapper);
        return users != null && users.size() != 0;
    }

    public List<User> selectAllUsers() {
        return userDao.selectList(null);
    }

    public User selectUserById(Integer id) {
        return userDao.selectById(id);
    }

    public User selectUserByEmail(String email) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(email != null, User::getEmail, email);
        List<User> users = userDao.selectList(wrapper);
        if (users == null || users.size() == 0) {
            return null;
        }
        return users.get(0);
    }

    public void insertUser(User user) {
        userDao.insert(user);
    }

    public void deleteUserById(Integer id) {
        userDao.deleteById(id);
    }
}
