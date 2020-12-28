package com.pjb.springbootjjwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.pjb.springbootjjwt.entity.User;
import com.pjb.springbootjjwt.interceptor.AuthenticationInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/**
 * @author jinbin
 * @date 2018-07-08 21:04
 */
@Service("TokenService")
public class TokenService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenService.class);

    public String getToken(User user) {
        /*String token="";
        token= JWT.create().withAudience(user.getId())       // 将 user id 保存到 token 里面
                .sign(Algorithm.HMAC256(user.getPassword()));// 以 password 作为 token 的密钥
        */
        String newToken = JWT
                .create()
                .withAudience(user.getId())
                .sign(Algorithm.HMAC512(user.getPassword())); // 使用password 作为密钥

        LOGGER.info("newToken="+newToken);
        return newToken;
    }
}
