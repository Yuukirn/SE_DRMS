package com.nqff.drms.middleware;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtUtils {

    public static final String SECRET_KEY = "noqueueforfood";
    public static final long EXPIRE_TIME = 5 * 60 * 1000;

    public static boolean verify(String token, String email) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            JWT.require(algorithm).withClaim("email", email).build().verify(token);
            return true;
        } catch (Exception e) {
            throw new JwtException(e.getMessage());
        }
    }

    public static String getUserEmail(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            String email = jwt.getClaim("email").toString();
            return email.substring(1, email.length() - 1);
        } catch (JWTDecodeException e) {
            throw new JwtException("token decode error");
        }
    }

    public static String sign(String email) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        return JWT.create().withClaim("email", email).withExpiresAt(date).sign(algorithm);
    }
}
