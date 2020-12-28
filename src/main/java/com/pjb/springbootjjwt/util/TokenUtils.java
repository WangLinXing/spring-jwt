package com.pjb.springbootjjwt.util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Auther: wangyognq
 * @Date: 2020/12/23
 * @Description: JWT 工具类 使用com.auth0.jwt实现
 * @Version: 1.0
 */
public class TokenUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenUtils.class);
    /** token秘钥，请勿泄露，请勿随便修改 backups:JKKLJOoasdlfj */
    private static final String SECRET = "JKKLJOoasdlfj";
    /** token 过期时间: 10秒 */

    //设置时间单位是秒
    public static final int calendarField = Calendar.SECOND;

    // 设置时间间隔，表示过期时间是10秒
    public static final int calendarInterval = 10;

    /**
     * JWT生成Token.<br/>
     * JWT构成: header, payload, signature
     * @param user_id 登录成功后用户user_id, 参数user_id不可传空
     */
    public static String createToken(Long user_id) throws Exception {
        Date iatDate = new Date();
        // 开设计算过期时间
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(calendarField, calendarInterval);
        Date expiresDate = nowTime.getTime();

        // header Map
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");

        // build token
        // param backups {iss:Service, aud:APP}
        String token = JWT.create()
                .withHeader(map) // header
                .withClaim("iss", "Service") // payload
                .withClaim("aud", "APP")
                .withClaim("user_id", null == user_id ? null : user_id.toString())
                .withIssuedAt(iatDate) // sign time
                .withExpiresAt(expiresDate) // 设置过期时间
                .sign(Algorithm.HMAC256(SECRET)); // signature

        return token;
    }

    /**
     * 解密Token
     *
     * @param token
     * @return
     * @throws Exception
     */
    public static Map<String, Claim> verifyToken(String token) {
        DecodedJWT jwt = null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            jwt = verifier.verify(token);
        } catch (JWTVerificationException e) {
            e.printStackTrace();
            // token 校验失败, 抛出Token验证非法异常
        }
        return jwt.getClaims();
    }

    /**
     * 根据Token获取user_id
     *
     * @param token
     * @return user_id
     */
    public static Long getAppUID(String token) {
        Map<String, Claim> claims = verifyToken(token);
        Claim user_id_claim = claims.get("user_id");
        if (null == user_id_claim || StringUtils.isEmpty(user_id_claim.asString())) {
            // token 校验失败, 抛出Token验证非法异常
        }
        return Long.valueOf(user_id_claim.asString());
    }

    public static void main(String[] argc) throws Exception{
        Long userid = 100L;
        String token = createToken(userid);
        //String token="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJBUFAiLCJ1c2VyX2lkIjoiMTAwIiwiaXNzIjoiU2VydmljZSIsInByaXZhdGVfdG9rZW4iOiJhYmNkZWZnaGlqa2xtIiwiZXhwIjoxNjA4NjQ0OTQ1LCJpYXQiOjE2MDg2NDQ5MzV9.ne5AN8gPg8ax344UbeOJaG5FZxryL0-y-6zYlUjpmsc";

        LOGGER.info("token="+ token);

        LOGGER.info("verifyToken___________");
        Map<String, Claim> verifyToken = verifyToken(token);
        for(String key:verifyToken.keySet()){
            if (StringUtils.isNotBlank(verifyToken.get(key).asString())){
                System.out.println(key+" ==> "+verifyToken.get(key).asString());
            }
        }
    }
}
