package com.zl.utils;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.zl.pojo.User;
import com.zl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class TokenUtils {

    private static UserService staticUserService;

    @Autowired
    private UserService userService;

    @PostConstruct
    public void setUserService() {
        staticUserService = userService;
    }

    /**
     * jwt资料：https://blog.csdn.net/TyearLin/article/details/121556725
     *
     * 创建token
     *
     * @param userId
     * @param sign
     * @return
     */
    public static String getToken(String userId, String sign){
        System.out.println("userId=======>"+userId);
        System.out.println("sign=======>"+sign);
        return JWT.create().withAudience(userId) //将userid保存到token里面，作为载荷
                /*DateUtil.offsetHour：当前时间往后偏移：第一个参数：时间， 第二个参数：偏移量，也就是往后偏移多久时间*/
                  .withExpiresAt(DateUtil.offsetHour(new Date(),2))//2小时后token过期，
                  .sign(Algorithm.HMAC256(sign));//以password 作为 token的密钥
    }


    /**
     * 获取当前登录的用户信息
     *
     * @return user对象
     */
    public static User getCurrentUser() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("token");
            if (StrUtil.isNotBlank(token)) {
                String userId = JWT.decode(token).getAudience().get(0);
                return staticUserService.getById(Integer.valueOf(userId));
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /*
    * 第一步：创建tokenUtils工具类，
    * 第二步：在用户登录时，调用login创建token，并携带token，返回的实体类中要有token字段
    * 第三步：在request.js中，编写request 拦截器，添加token在请求头中
    * 第四步：在后端创建JwtInterceptor拦截器
    * 第五步：创建InterceptorConfig配置类，让拦截器生效
    * 第六步：在request.js中编写错误提示代码
    * */
}
