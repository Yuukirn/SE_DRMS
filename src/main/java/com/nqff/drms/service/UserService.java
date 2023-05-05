package com.nqff.drms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nqff.drms.pojo.User;


public interface UserService extends IService<User> {
    boolean isUserExisted(String email);

    User selectUserByEmail(String email);

    void insertUser(User user);

    // TODO: page
}
