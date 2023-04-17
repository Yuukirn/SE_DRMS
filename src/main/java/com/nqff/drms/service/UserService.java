package com.nqff.drms.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nqff.drms.dao.UserDao;
import com.nqff.drms.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService extends IService<User> {
    boolean isUserExisted(String email);

    User selectUserByEmail(String email);

    // TODO: page
}
