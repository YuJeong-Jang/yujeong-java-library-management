package com.book.library.frontend.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        
        String requestURI = request.getRequestURI();
        
        // 로그인이 필요없는 경로들
        if (requestURI.equals("/") || 
            requestURI.equals("/login") || 
            requestURI.equals("/register") ||
            requestURI.equals("/logout") ||
            requestURI.startsWith("/css/") ||
            requestURI.startsWith("/js/") ||
            requestURI.startsWith("/images/") ||
            requestURI.equals("/favicon.ico") ||
            requestURI.equals("/error")) {
            return true;
        }
        
        // 세션이 없거나 로그인 정보가 없으면 로그인 페이지로 리다이렉트
        if (session == null || session.getAttribute("loginMember") == null) {
            response.sendRedirect("/login?error=login_required");
            return false;
        }
        
        return true;
    }
}