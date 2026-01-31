package com.book.library.frontend.config;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Map;

@ControllerAdvice
public class GlobalControllerAdvice {
    
    @ModelAttribute("loginMember")
    public Map<String, Object> loginMember(HttpSession session) {
        Map<String, Object> loginMember = (Map<String, Object>) session.getAttribute("loginMember");
        return loginMember;
    }
    
    @ModelAttribute("isLoggedIn")
    public boolean isLoggedIn(HttpSession session) {
        Map<String, Object> loginMember = (Map<String, Object>) session.getAttribute("loginMember");
        boolean loggedIn = loginMember != null;
        return loggedIn;
    }
}