package com.nqff.drms.controller;

import com.nqff.drms.middleware.JWTUtils;
import com.nqff.drms.pojo.User;
import com.nqff.drms.service.EmailService;
import com.nqff.drms.service.UserService;
import com.nqff.drms.utils.RandomCode;
import com.nqff.drms.utils.Result;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(path = "/")
public class UserController {

    private final int REDIS_TIMEOUT = 300;
    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @PostMapping(path = "/register-code")
    public Result<String> SendRegisterCode(@RequestBody User user) {
        if (userService.isUserExisted(user.getEmail())) {
            return Result.SUCCESS("registered");
        }
        String code = RandomCode.GenerateRandomCode();
        redisTemplate.opsForValue().set(user.getEmail(), code, REDIS_TIMEOUT, TimeUnit.SECONDS);
        emailService.SendMail(user.getEmail(), "注册验证码", code);
        return Result.SUCCESS(code);
    }

    @PostMapping(path = "/register")
    public Result UserRegister(@RequestBody Map request) {
        String code = (String)request.get("code");
        String email = (String)request.get("email");
        String true_code = redisTemplate.opsForValue().get(email);
        if (!Objects.equals(code, true_code)) {
            return Result.FAIL("wrong register code", null);
        }
        String password = (String)request.get("password");
        String md5_pwd = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        String name = (String)request.get("name");
        User user = new User(name, email, md5_pwd);
        userService.insertUser(user);
        return Result.SUCCESS(null);
    }

    @PostMapping(path = "/login")
    public Result UserLogin(@RequestBody Map request) {
        String email = (String)request.get("email");
        if (!userService.isUserExisted(email)) {
            return Result.FAIL("not registered", null);
        }
        String password = (String)request.get("password");
        String md5_pwd = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        User user = userService.selectUserByEmail(email);
        if (!Objects.equals(md5_pwd, user.getPassword())) {
            return Result.FAIL("wrong password", null);
        }

        return Result.SUCCESS(JWTUtils.sign(email, user.getPassword()));
    }

    @GetMapping(path = "/user/{id}")
    public Result<User> getUserById(@PathVariable Integer id) {
        User user = userService.selectUserById(id);
        if (user == null) {
            return Result.FAIL("not found", null);
        }
        return Result.SUCCESS(user);
    }

    @GetMapping(path = "/user")
    @RequiresAuthentication
    public Result<List<User>> getAllUsers() {
        List<User> users = userService.selectAllUsers();
        if (users == null || users.size() == 0) {
            return Result.FAIL("not found", null);
        }
        return Result.SUCCESS(users);
    }

    @DeleteMapping(path = "/user/{id}")
    public Result deleteUserById(@PathVariable Integer id) {
        userService.deleteUserById(id);
        return Result.SUCCESS(null);
    }
}
