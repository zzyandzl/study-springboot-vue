package com.zl.interceptor;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.zl.common.Constants;
import com.zl.common.ServiceException;
import com.zl.pojo.User;
import com.zl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*从请求头中获取token*/
        String token = request.getHeader("token");
//       如果不是映射方法(直接调用方法)则直接通过
        if(!(handler instanceof HandlerMethod)){
            return true;
        }

        // 执行认证
        if(StrUtil.isBlank(token)){
            throw new ServiceException(Constants.CODE_101.getCode(),"无token,请重新登录");
        }

        // 获取 token 中的 user id
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);
        }catch (JWTDecodeException e){
            throw new ServiceException(Constants.CODE_101.getCode(),"token验证失败，请重新登录");
        }

        // 根据token中的userid查询数据库
        User user = userService.getById(Integer.parseInt(userId));
        if(user == null){
            throw new ServiceException(Constants.CODE_101.getCode(),"用户不存在，请重新登录");
        }

        // 用户密码加签校验token是否正确
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try {
            jwtVerifier.verify(token); // 验证token
        } catch (JWTVerificationException e) {
            throw new ServiceException(Constants.CODE_101.getCode(), "token验证失败，请重新登录");
        }
        return true;
    }
}
