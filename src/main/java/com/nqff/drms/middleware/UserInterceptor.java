package com.nqff.drms.middleware;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nqff.drms.dao.UserDao;
import com.nqff.drms.pojo.User;
import com.nqff.drms.service.UserService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;
import java.util.Objects;

@Component
public class UserInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws JwtException {
        String token = request.getHeader("Authorization");
        if (token == null || token.equals("")) {
            throw new JwtException("no token");
        }
        token = token.substring(7);
        String email = JwtUtils.getUserEmail(token);

        if (!JwtUtils.verify(token, email)) {
            throw new JwtException("token expire");
        }

        return true;
    }
}
