package com.pjb.springbootjjwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.pjb.springbootjjwt.interceptor.AuthenticationInterceptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootJjwtApplicationTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringbootJjwtApplicationTests.class);

    @Test
    public void contextLoads() {
        System.out.println("aa");
    }

    @Test
    public void testCreateJWT(){
        String userId = "10";
        String password = "123457";

        Algorithm hmac512 = Algorithm.HMAC512(password);
        LOGGER.info("name="+hmac512.getName());
        LOGGER.info("SigningKeyId="+hmac512.getSigningKeyId());
        String newToken = JWT
                .create()
                .withAudience(userId)
                .sign(hmac512); // 使用password 作为密钥

        LOGGER.info("newToken="+newToken);

        JWTVerifier jwtVerifier = JWT.require(hmac512).build();
        try {
            jwtVerifier.verify(newToken);
            LOGGER.info("Token OK");
        } catch (JWTVerificationException e) {
            throw new RuntimeException("401");
        }


    }

}
