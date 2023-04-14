package com.nqff.drms.mapper;

import com.nqff.drms.pojo.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    List<User> selectAll();
    User selectUserByEmail(String email);
    User selectUserById(Integer id);

    void insertUser(String name, String email, String password);

    void deleteUserById(Integer id);
}
