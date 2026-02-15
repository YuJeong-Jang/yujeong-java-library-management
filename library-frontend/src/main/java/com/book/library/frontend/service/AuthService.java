package com.book.library.frontend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    @Qualifier("memberServiceUrl")
    private final String memberServiceUrl;
    
    @SuppressWarnings("unchecked")
    public Map<String, Object> login(String loginId, String password) {
        try {
            Map<String, String> loginRequest = Map.of(
                "loginId", loginId,
                "password", password
            );
            
            HttpPost request = new HttpPost(memberServiceUrl + "/api/members/login");
            String jsonBody = objectMapper.writeValueAsString(loginRequest);
            request.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));
            
            return httpClient.execute(request, response -> {
                String responseBody = EntityUtils.toString(response.getEntity());
                
                if (response.getCode() >= 400) {
                    if (response.getCode() == 401) {
                        return Map.of("success", false, "message", "아이디 또는 비밀번호가 올바르지 않습니다.");
                    }
                    return Map.of("success", false, "message", "로그인 중 오류가 발생했습니다.");
                }
                
                return objectMapper.readValue(responseBody, Map.class);
            });
        } catch (Exception e) {
            return Map.of("success", false, "message", "로그인 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    public Map<String, Object> register(Map<String, Object> memberData) {
        try {
            HttpPost request = new HttpPost(memberServiceUrl + "/api/members");
            String jsonBody = objectMapper.writeValueAsString(memberData);
            request.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));
            
            return httpClient.execute(request, response -> {
                String responseBody = EntityUtils.toString(response.getEntity());
                
                if (response.getCode() >= 400) {
                    Map<String, Object> errorResult = objectMapper.readValue(responseBody, Map.class);
                    String errorMessage = "회원가입에 실패했습니다.";
                    if (errorResult.get("message") != null) {
                        errorMessage = errorResult.get("message").toString();
                    }
                    return Map.of("success", false, "message", errorMessage);
                }
                
                return objectMapper.readValue(responseBody, Map.class);
            });
        } catch (Exception e) {
            return Map.of("success", false, "message", "회원가입 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}