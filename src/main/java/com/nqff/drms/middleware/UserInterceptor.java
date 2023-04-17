package com.nqff.drms.middleware;

import com.nqff.drms.pojo.User;
import com.nqff.drms.service.UserService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

@Component
public class UserInterceptor implements HandlerInterceptor {
    private static UserService userService;

    @Autowired
    public void setUserService(UserService service) {
        UserInterceptor.userService = service;
    }


    private static RedisTemplate<String, String> redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        UserInterceptor.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws JwtException {
        String token = request.getHeader("Authorization");
        if (token == null || token.equals("")) {
            throw new JwtException("no token");
        }
        token = token.substring(7);
//        System.out.println("get token: " + token.length());
        String email = JwtUtils.getUserEmail(token);
        User user = userService.selectUserByEmail(email);
//        String true_token = redisTemplate.opsForValue().get(user.getEmail() + "_token");
//        System.out.println(true_token);
//        System.out.println(token);
//        if (Objects.equals(true_token, token)) {
//            System.out.println("same!!");
//        } else {
//            System.out.println("not same!!");
//        }
        if (user == null) {
            throw new JwtException("no user");
        }
        if (!JwtUtils.verify(token, email, user.getPassword())) {
            throw new JwtException("token expire");
        }

        return true;
    }
}
