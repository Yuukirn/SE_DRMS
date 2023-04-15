package com.nqff.drms.middleware;

import com.nqff.drms.pojo.User;
import com.nqff.drms.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class JWTRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String)authenticationToken.getCredentials();
        String email = JWTUtils.getUserEmail(token);
        if (email == null) {
            throw new AuthenticationException("token invalid");
        }

        User user = userService.selectUserByEmail(email);
        if (user == null) {
            throw new AuthenticationException("user didn't existed");
        }

        if (!JWTUtils.verify(token, email, user.getPassword())) {
            throw new AuthenticationException("token verify failed");
        }
        return new SimpleAuthenticationInfo(token, token, "JWTRealm");
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("AuthorizationInfo");
        return null;
    }
}
