package com.book.library.frontend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class MemberService {
    
    private final WebClient memberServiceClient;
    
    @Value("${library.member.url}")
    private String memberUrl;
    
    public MemberService(@Qualifier("memberServiceWebClient") WebClient memberServiceClient) {
        this.memberServiceClient = memberServiceClient;
    }
    
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getAllMembers() {
        try {
            Map<String, Object> response = memberServiceClient
                    .get()
                    .uri("/api/members")
                    .retrieve()
                    .bodyToMono(Map.class)
                    .onErrorReturn(Map.of("success", false, "data", List.of()))
                    .block();
            
            if (response != null && Boolean.TRUE.equals(response.get("success"))) {
                List<Map<String, Object>> allMembers = (List<Map<String, Object>>) response.get("data");
                // 모든 회원 반환 (활성/비활성 모두 포함)
                return allMembers != null ? allMembers : List.of();
            }
            return List.of();
        } catch (Exception e) {
            return List.of();
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getActiveMembers() {
        try {
            Map<String, Object> response = memberServiceClient
                    .get()
                    .uri("/api/members")
                    .retrieve()
                    .bodyToMono(Map.class)
                    .onErrorReturn(Map.of("success", false, "data", List.of()))
                    .block();
            
            if (response != null && Boolean.TRUE.equals(response.get("success"))) {
                List<Map<String, Object>> allMembers = (List<Map<String, Object>>) response.get("data");
                // 활성 회원만 필터링 (ACTIVE 상태 또는 status == 0)
                return allMembers.stream()
                    .filter(member -> {
                        Object status = member.get("status");
                        return "ACTIVE".equals(status) || Integer.valueOf(0).equals(status);
                    })
                    .toList();
            }
            return List.of();
        } catch (Exception e) {
            return List.of();
        }
    }
    
    @SuppressWarnings("unchecked")
    public Map<String, Object> getMember(Long id) {
        try {
            Map<String, Object> response = memberServiceClient
                    .get()
                    .uri("/api/members/" + id)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .onErrorReturn(Map.of("success", false, "message", "회원을 찾을 수 없습니다."))
                    .block();
            
            if (response != null && Boolean.TRUE.equals(response.get("success"))) {
                return (Map<String, Object>) response.get("data");
            }
            return Map.of();
        } catch (Exception e) {
            return Map.of();
        }
    }
    
    public Map<String, Object> changeRole(Long id, Map<String, Object> request) {
        try {
            Map<String, Object> response = memberServiceClient
                    .patch()
                    .uri("/api/members/" + id + "/role")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .onErrorReturn(Map.of("success", false, "message", "권한 변경에 실패했습니다."))
                    .block();
            
            return response != null ? response : Map.of("success", false, "message", "응답이 없습니다.");
        } catch (Exception e) {
            return Map.of("success", false, "message", "권한 변경 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    public Map<String, Object> createMember(Map<String, Object> memberData) {
        try {
            Map<String, Object> response = memberServiceClient
                    .post()
                    .uri("/api/members")
                    .bodyValue(memberData)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), clientResponse -> {
                        return clientResponse.bodyToMono(Map.class)
                                .map(errorBody -> {
                                    String errorMessage = "회원 등록에 실패했습니다.";
                                    if (errorBody != null && errorBody.get("message") != null) {
                                        errorMessage = errorBody.get("message").toString();
                                    }
                                    return new RuntimeException("API_ERROR:" + errorMessage);
                                });
                    })
                    .bodyToMono(Map.class)
                    .onErrorResume(throwable -> {
                        String errorMessage = "회원 등록에 실패했습니다.";
                        if (throwable.getMessage() != null && throwable.getMessage().startsWith("API_ERROR:")) {
                            errorMessage = throwable.getMessage().substring("API_ERROR:".length());
                        }
                        return reactor.core.publisher.Mono.just(Map.of("success", false, "message", errorMessage));
                    })
                    .block();
            
            return response != null ? response : Map.of("success", false, "message", "응답이 없습니다.");
        } catch (Exception e) {
            return Map.of("success", false, "message", "회원 등록 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    public Map<String, Object> updateMember(Long id, Map<String, Object> memberData) {
        try {
            Map<String, Object> response = memberServiceClient
                    .put()
                    .uri("/api/members/" + id)
                    .bodyValue(memberData)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .onErrorReturn(Map.of("success", false, "message", "회원 수정에 실패했습니다."))
                    .block();
            
            return response != null ? response : Map.of("success", false, "message", "응답이 없습니다.");
        } catch (Exception e) {
            return Map.of("success", false, "message", "회원 수정 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    public Map<String, Object> deleteMember(Long id) {
        try {
            Map<String, Object> response = memberServiceClient
                    .delete()
                    .uri("/api/members/" + id)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError(), clientResponse -> {
                        return clientResponse.bodyToMono(String.class)
                                .map(body -> new RuntimeException("Client error: " + clientResponse.statusCode() + " - " + body));
                    })
                    .onStatus(status -> status.is5xxServerError(), clientResponse -> {
                        return clientResponse.bodyToMono(String.class)
                                .map(body -> {
                                    // 외래키 제약조건 오류 체크
                                    if (body.contains("foreign key constraint fails") && body.contains("rental")) {
                                        return new RuntimeException("FOREIGN_KEY_CONSTRAINT_RENTAL");
                                    }
                                    return new RuntimeException("Server error: " + clientResponse.statusCode() + " - " + body);
                                });
                    })
                    .bodyToMono(Map.class)
                    .onErrorResume(throwable -> {
                        if (throwable.getMessage() != null && throwable.getMessage().contains("FOREIGN_KEY_CONSTRAINT_RENTAL")) {
                            return Mono.just(Map.of("success", false, "message", "대여 기록이 있는 회원은 삭제할 수 없습니다. 먼저 모든 대여를 반납하거나 회원을 비활성화해주세요.", "errorType", "FOREIGN_KEY_CONSTRAINT"));
                        }
                        return Mono.just(Map.of("success", false, "message", "회원 삭제에 실패했습니다."));
                    })
                    .block();
            
            return response != null ? response : Map.of("success", false, "message", "응답이 없습니다.");
        } catch (Exception e) {
            // 외래키 제약조건 오류 체크
            if (e.getMessage() != null && (e.getMessage().contains("foreign key constraint fails") || 
                e.getMessage().contains("FOREIGN_KEY_CONSTRAINT_RENTAL"))) {
                return Map.of("success", false, "message", "대여 기록이 있는 회원은 삭제할 수 없습니다. 먼저 모든 대여를 반납하거나 회원을 비활성화해주세요.", "errorType", "FOREIGN_KEY_CONSTRAINT");
            }
            
            return Map.of("success", false, "message", "회원 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    public Map<String, Object> deactivateMember(Long id) {
        try {
            Map<String, Object> response = memberServiceClient
                    .patch()
                    .uri("/api/members/" + id + "/deactivate")
                    .retrieve()
                    .bodyToMono(Map.class)
                    .onErrorReturn(Map.of("success", false, "message", "회원 비활성화에 실패했습니다."))
                    .block();
            
            return response != null ? response : Map.of("success", false, "message", "응답이 없습니다.");
        } catch (Exception e) {
            return Map.of("success", false, "message", "회원 비활성화 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    public Map<String, Object> activateMember(Long id) {
        try {
            Map<String, Object> response = memberServiceClient
                    .patch()
                    .uri("/api/members/" + id + "/activate")
                    .retrieve()
                    .bodyToMono(Map.class)
                    .onErrorReturn(Map.of("success", false, "message", "회원 활성화에 실패했습니다."))
                    .block();
            
            return response != null ? response : Map.of("success", false, "message", "응답이 없습니다.");
        } catch (Exception e) {
            return Map.of("success", false, "message", "회원 활성화 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    public Map<String, Object> makeAdmin(Long id) {
        try {
            Map<String, Object> updateData = Map.of("role", 1); // ADMIN = 1
            Map<String, Object> response = memberServiceClient
                    .patch()
                    .uri("/api/members/" + id + "/role")
                    .bodyValue(updateData)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .onErrorReturn(Map.of("success", false, "message", "관리자 권한 부여에 실패했습니다."))
                    .block();
            
            return response != null ? response : Map.of("success", false, "message", "응답이 없습니다.");
        } catch (Exception e) {
            return Map.of("success", false, "message", "관리자 권한 부여 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}