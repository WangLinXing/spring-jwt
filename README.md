# springboot-jwt
SpringBoot集成JWT实现token验证
此工程有以下知识点：
- 1 定义注解
    - a PassToken 方法级注解，此注解可以使得某个请求跳过tokan验证
    - b UserLoginToken 方法级注解，在方法上标注这个注解时，表示访问这个方法时必须进行token验证
    
- 2 定义AuthenticationInterceptor拦截器
    - 判断当前方法是否需要被拦截
    - 判断当前方法是否可以

- 3 全局异常处理
- 4 集成mybatis
- 5 用户登录时创建token
- 6 提供了另外两种SpringBoot集成JWT实现token验证的工具类
    - JwtUtil
        使用io.jsonwebtoken实现，可以增加用户自定义数据以及token过期时间
    - TokenUtils
        使用com.auth0实现，可以增加用户自定义数据以及token过期时间

##### 博客地址：https://www.jianshu.com/p/e88d3f8151db

