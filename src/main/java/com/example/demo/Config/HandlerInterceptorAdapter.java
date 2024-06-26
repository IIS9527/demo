package com.example.demo.Config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public abstract class HandlerInterceptorAdapter implements AsyncHandlerInterceptor {

    // 在目标方法执行前执行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            return true;
        }
        // 获取注解
        Auth auth = ((HandlerMethod) handler).getMethod().getAnnotation(Auth.class);
        if (auth == null) {
            return true;
        }
        // 从参数中取出用户身份并验证
        String admin = auth.user();
        if (!admin.equals(request.getParameter("user"))) {
            response.setStatus(403);
            return false;
        }
        return true;
    }

}