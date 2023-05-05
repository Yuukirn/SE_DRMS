package com.nqff.drms.controller;

import com.alibaba.fastjson.JSONObject;
import com.nqff.drms.middleware.JwtUtils;
import com.nqff.drms.pojo.User;
import com.nqff.drms.service.EmailService;
import com.nqff.drms.service.UserService;
import com.nqff.drms.utils.RandomCode;
import com.nqff.drms.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(path = "/")
@Tag(name = "用户接口")
public class UserController {

    private final int REDIS_TIMEOUT = 300;
    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Operation(summary = "发送验证码", description = "json: email")
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

    @Operation(summary = "用户注册", description = "json: code, email, password, name")
    @PostMapping(path = "/register")
    public Result UserRegister(@RequestBody JSONObject request) {
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

    @Operation(summary = "用户登陆", description = "json: email, password")
    @PostMapping(path = "/login")
    public Result UserLogin(@RequestBody User user) {
        String email = user.getEmail();
        if (!userService.isUserExisted(email)) {
            return Result.FAIL("not registered", null);
        }
        String password = user.getPassword();
        String md5_pwd = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        User db_user = userService.selectUserByEmail(email);
        if (!Objects.equals(md5_pwd, db_user.getPassword())) {
            return Result.FAIL("wrong password", null);
        }
        String redis_key = email + "_token";
        String token = null;
        if (redisTemplate.opsForValue().get(redis_key) == null) {
            token = JwtUtils.sign(email);
            redisTemplate.opsForValue().set(redis_key, token, JwtUtils.EXPIRE_TIME / 4 * 3, TimeUnit.MILLISECONDS);
        }

        Map<String, Object> res = new HashMap<>();
        res.put("user_id", db_user.getId());
        if (token != null) {
            res.put("token", token);
        }

        return Result.SUCCESS(res);
    }

    @Operation(summary = "根据 id 获取用户信息", security = {@SecurityRequirement(name = "Authorization")})
    @GetMapping(path = "/users/{id}")
    public Result<User> getUserById(@PathVariable Integer id) {
        User user = userService.getById(id);
        if (user == null) {
            return Result.FAIL("not found", null);
        }
        return Result.SUCCESS(user);
    }

    @Operation(summary = "获取所有用户信息", security = {@SecurityRequirement(name = "Authorization")})
    @GetMapping(path = "/users")
    public Result<List<User>> getAllUsers() {
        List<User> users = userService.list();
        if (users == null || users.size() == 0) {
            return Result.FAIL("not found", null);
        }
        return Result.SUCCESS(users);
    }

    @Operation(summary = "根据 id 删除用户", security = {@SecurityRequirement(name = "Authorization")})
    @DeleteMapping(path = "/users/{id}")
    public Result deleteUserById(@PathVariable Integer id) {
        userService.removeById(id);
        return Result.SUCCESS(null);
    }

    @Operation(summary = "更新用户数据", security = {@SecurityRequirement(name = "Authorization")})
    @PutMapping(path = "/users")
    public Result updateUserInfo(@RequestBody User user) {
        userService.updateById(user);
        return Result.SUCCESS(null);
    }
}
