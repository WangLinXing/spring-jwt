package com.pjb.springbootjjwt.util;

import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Auther: wangyognq
 * @Date: 2020/12/23
 * @Description: 使用了io.jsonwebtoken实现了JWT
 * 参考
 * https://codechina.csdn.net/mirrors/jwtk/jjwt?utm_medium=distribute.pc_aggpage_search_result.none-task-code_china-2~aggregatepage~first_rank_v2~rank_v29-1-6192.nonecase&utm_term=io.jsonwebtoken&spm=1000.2123.3001.4430
 * @Version: 1.0
 */
public class JwtUtil {
    private static final String JWT_ID = UUID.randomUUID().toString();
    //加密密文
    public static final String JWT_SECRET = "1oov55mpqcrqnouup4mchar2ubg5j4hdfldabf3k7094ia0vlj86";
    public static final int JWT_TTL = 60*60*1000;  //mi

    public SecretKey generalKey() {
        // 本地的密码解码
        byte[] encodedKey = Base64.decodeBase64(JWT_SECRET);
        // 根据给定的字节数组使用AES加密算法构造一个密钥
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }
    /**
     * 创建jwt
     * @param id
     * @param issuer
     * @param subject
     * @param ttlMillis
     * @return
     * @throws Exception
     */
    public String createJWT(String id, String issuer, String subject, long ttlMillis) throws Exception {

        // 指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // 生成JWT的时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // 创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）
        Map<String, Object> claims = new HashMap<>();
        claims.put("userMobile", "186");
        claims.put("createDate", "2019-10-22");
        claims.put("userName", "zhangsan");
        claims.put("userCompany", "公司");
        claims.put("userCountry", "国家");

        // 生成签名的时候使用的秘钥secret，切记这个秘钥不能外露哦。它就是你服务端的私钥，在任何场景都不应该流露出去。
        // 一旦客户端得知这个secret, 那就意味着客户端是可以自我签发jwt了。
        SecretKey key = generalKey();

        // 下面就是在为payload添加各种标准声明和私有声明了
        JwtBuilder builder = Jwts.builder() // 这里其实就是new一个JwtBuilder，设置jwt的body
                .setClaims(claims)          // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setId(id)                  // 设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setIssuedAt(now)           // iat: jwt的签发时间
                .setIssuer(issuer)          // issuer：jwt签发人
                .setSubject(subject)        // sub(Subject)：代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
                .signWith(signatureAlgorithm, key); // 设置签名使用的签名算法和签名使用的秘钥

        // 设置过期时间
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    /**
     * 解密jwt
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public Claims parseJWT(String jwt) throws Exception {
        SecretKey key = generalKey();  //签名秘钥，和生成的签名的秘钥一模一样
        Claims claims = Jwts.parser()  //得到DefaultJwtParser
                .setSigningKey(key)                 //设置签名的秘钥
                .parseClaimsJws(jwt).getBody();     //设置需要解析的jwt
        return claims;
    }

    private Claims parseJWT1(String jwt) {
        SecretKey key = generalKey();
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(String.valueOf(key)))
                .parseClaimsJws(jwt).getBody();
        System.out.println("ID: " + claims.getId());
        System.out.println("Subject: " + claims.getSubject());
        System.out.println("Issuer: " + claims.getIssuer());
        System.out.println("Expiration: " + claims.getExpiration());
        return claims;
    }

    public static void main(String[] args) {
        Map<String,String> map = new HashMap<String,String>();
        map.put("userid","111");
        map.put("roleid","45");
        String subject = JSONObject.toJSONString(map);

        try {
            JwtUtil util = new JwtUtil();

            String jwt = util.createJWT(JWT_ID, "Anson", subject, JWT_TTL);
            //System.out.println("JWT：" + jwt);

            System.out.println("\n解密\n");

            Claims c = util.parseJWT(jwt);

            System.out.println(c.getId()+"==111");
            System.out.println(c.getIssuedAt()+"==222");
            System.out.println("json="+c.getSubject());
            System.out.println(c.getIssuer()+"==4444");
            System.out.println("userMobile="+ c.get("userMobile", String.class));
            System.out.println("userName="+ c.get("userName", String.class));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
