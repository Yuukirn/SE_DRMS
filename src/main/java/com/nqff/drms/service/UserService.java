package com.nqff.drms.service;

import com.nqff.drms.mapper.UserMapper;
import com.nqff.drms.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public boolean isUserExisted(String email) {
        User user = userMapper.selectUserByEmail(email);
        return user != null;
    }

    public List<User> selectAllUsers() {
        return userMapper.selectAll();
    }

    public User selectUserById(Integer id) {
        return userMapper.selectUserById(id);
    }

    public User selectUserByEmail(String email) {
        return userMapper.selectUserByEmail(email);
    }

    public void insertUser(User user) {
        userMapper.insertUser(user.getName(), user.getEmail(), user.getPassword());
    }

    public void deleteUserById(Integer id) {
        userMapper.deleteUserById(id);
    }
}
