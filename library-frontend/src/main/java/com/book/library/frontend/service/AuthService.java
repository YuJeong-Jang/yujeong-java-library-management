package com.book.library.frontend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;

@Service
public class AuthService {
    
    private final WebClient memberServiceClient;
    
    public AuthService(@Qualifier("memberServiceWebClient") WebClient memberServiceClient) {
        this.memberServiceClient = memberServiceClient;
    }
    
    @SuppressWarnings("unchecked")
    public Map<String, Object> login(String loginId, String password) {
        try {
            // 로그인 검증 API 사용
            Map<String, String> loginRequest = Map.of(
                "loginId", loginId,
                "password", password
            );
            
            Map<String, Object> response = memberServiceClient
                    .post()
                    .uri("/api/members/login")
                    .bodyValue(loginRequest)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response != null && Boolean.TRUE.equals(response.get("success"))) {
                Map<String, Object> member = (Map<String, Object>) response.get("data");
                        
                // 로그인 성공
                return Map.of(
                    "success", true,
                    "message", response.get("message"),
                    "data", member
                );
            } else {
                return Map.of(
                    "success", false,
                    "message", response.get("message")
                );
            }
            
        } catch (WebClientResponseException e) {
            return Map.of(
                "success", false,
                "message", "로그인 중 오류가 발생했습니다: " + e.getMessage()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of(
                "success", false,
                "message", "로그인 중 오류가 발생했습니다: " + e.getMessage()
            );
        }
    }
    
    public Map<String, Object> register(Map<String, Object> memberData) {
        try {
            // 역할 설정 (0: USER, 1: ADMIN)
            Integer role = (Integer) memberData.get("role");
            if (role == null) {
                role = 0; // 기본값은 일반 사용자
            }
            
            // phone 필드 처리 - 빈 문자열이면 null로 설정
            String phone = (String) memberData.get("phone");
            if (phone != null && phone.trim().isEmpty()) {
                phone = null;
            }
            
            // 회원 데이터 준비 - null 값 제외
            Map<String, Object> requestData = new java.util.HashMap<>();
            requestData.put("loginId", memberData.get("loginId"));
            requestData.put("password", memberData.get("password"));
            requestData.put("name", memberData.get("name"));
            requestData.put("email", memberData.get("email"));
            requestData.put("role", role);
            
            // phone이 null이 아닌 경우만 추가
            if (phone != null && !phone.trim().isEmpty()) {
                requestData.put("phone", phone);
            }
            
            Map<String, Object> response = memberServiceClient
                    .post()
                    .uri("/api/members")
                    .bodyValue(requestData)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            
            // ApiResponse 형식의 응답을 그대로 반환
            if (response != null) {
                return response;
            } else {
                return Map.of("success", false, "message", "서버로부터 응답이 없습니다.");
            }
            
        } catch (WebClientResponseException e) {
            // HTTP 에러 응답 처리
            try {
                // 에러 응답 본문을 파싱하여 반환
                String responseBody = e.getResponseBodyAsString();
                if (responseBody != null && !responseBody.isEmpty()) {
                    // JSON 파싱 없이 간단한 에러 메시지 반환
                    return Map.of(
                        "success", false, 
                        "message", "회원가입에 실패했습니다. 상태코드: " + e.getStatusCode() + ", 응답: " + responseBody
                    );
                }
            } catch (Exception parseException) {
                // 파싱 실패시 기본 에러 메시지
            }
            
            return Map.of(
                "success", false,
                "message", "회원가입 중 오류가 발생했습니다: " + e.getMessage()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of(
                "success", false,
                "message", "회원가입 중 오류가 발생했습니다: " + e.getMessage()
            );
        }
    }
}