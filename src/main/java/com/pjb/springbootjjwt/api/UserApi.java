package com.pjb.springbootjjwt.api;

import com.alibaba.fastjson.JSONObject;
import com.pjb.springbootjjwt.annotation.PassToken;
import com.pjb.springbootjjwt.annotation.UserLoginToken;
import com.pjb.springbootjjwt.entity.User;
import com.pjb.springbootjjwt.service.TokenService;
import com.pjb.springbootjjwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author jinbin
 * @date 2018-07-08 20:45
 */
@RestController
@RequestMapping("api")
public class UserApi {
    @Autowired
    UserService userService;
    @Autowired
    TokenService tokenService;
    //登录
    @PostMapping("/login")
    public Object login(User user,HttpServletResponse response){
        JSONObject jsonObject=new JSONObject();
        System.out.println("username="+ user.getUsername());
        User userForBase=userService.findByUsername(user);
        if(userForBase==null){
            jsonObject.put("message","登录失败,用户不存在1");
            return jsonObject;
        }else {
            if (!userForBase.getPassword().equals(user.getPassword())){
                jsonObject.put("message","登录失败,密码错误");
                return jsonObject;
            }else {
                String token = tokenService.getToken(userForBase);
                jsonObject.put("token", token);
                jsonObject.put("user", userForBase);
                /*Cookie cookie = new Cookie("JSESSIONID","testValue");
                cookie.setHttpOnly(true);
                cookie.setMaxAge(60);
                cookie.setDomain("localhost");
                cookie.setPath("/");
                response.addCookie(cookie);
                */
                return jsonObject;
            }
        }
    }
    
    @UserLoginToken
    @GetMapping("/getMessage")
    public String getMessage(){
        return "你已通过验证";
    }

    @UserLoginToken // 指定需要进行token验证
    @GetMapping("/test")
    public String getInfo(){
        return "test message";
    }

    @PassToken // 指定当前方法跳过tokan验证
    @GetMapping("/getUserInfo")
    public String  getCurrentDatetime(){
        String date = new Date().toString();
        return date;
    }
}
