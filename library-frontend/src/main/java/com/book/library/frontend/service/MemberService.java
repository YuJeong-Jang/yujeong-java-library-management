package com.book.library.frontend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MemberService {
    
    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    @Qualifier("memberServiceUrl")
    private final String memberServiceUrl;
    
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getAllMembers() {
        try {
            HttpGet request = new HttpGet(memberServiceUrl + "/api/members");
            
            return httpClient.execute(request, response -> {
                String responseBody = EntityUtils.toString(response.getEntity());
                Map<String, Object> result = objectMapper.readValue(responseBody, Map.class);
                
                if (Boolean.TRUE.equals(result.get("success"))) {
                    List<Map<String, Object>> allMembers = (List<Map<String, Object>>) result.get("data");
                    return allMembers != null ? allMembers : List.of();
                }
                return List.of();
            });
        } catch (Exception e) {
            return List.of();
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getActiveMembers() {
        try {
            HttpGet request = new HttpGet(memberServiceUrl + "/api/members");
            
            return httpClient.execute(request, response -> {
                String responseBody = EntityUtils.toString(response.getEntity());
                Map<String, Object> result = objectMapper.readValue(responseBody, Map.class);
                
                if (Boolean.TRUE.equals(result.get("success"))) {
                    List<Map<String, Object>> allMembers = (List<Map<String, Object>>) result.get("data");
                    return allMembers.stream()
                        .filter(member -> {
                            Object status = member.get("status");
                            return "ACTIVE".equals(status) || Integer.valueOf(0).equals(status);
                        })
                        .toList();
                }
                return List.of();
            });
        } catch (Exception e) {
            return List.of();
        }
    }
    
    @SuppressWarnings("unchecked")
    public Map<String, Object> getMember(Long id) {
        try {
            HttpGet request = new HttpGet(memberServiceUrl + "/api/members/" + id);
            
            return httpClient.execute(request, response -> {
                String responseBody = EntityUtils.toString(response.getEntity());
                Map<String, Object> result = objectMapper.readValue(responseBody, Map.class);
                
                if (Boolean.TRUE.equals(result.get("success"))) {
                    return (Map<String, Object>) result.get("data");
                }
                return Map.of();
            });
        } catch (Exception e) {
            return Map.of();
        }
    }
    
    public Map<String, Object> createMember(Map<String, Object> memberData) {
        try {
            HttpPost request = new HttpPost(memberServiceUrl + "/api/members");
            String jsonBody = objectMapper.writeValueAsString(memberData);
            request.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));
            
            return httpClient.execute(request, response -> {
                String responseBody = EntityUtils.toString(response.getEntity());
                
                if (response.getCode() >= 400) {
                    Map<String, Object> errorResult = objectMapper.readValue(responseBody, Map.class);
                    String errorMessage = "회원 등록에 실패했습니다.";
                    if (errorResult.get("message") != null) {
                        errorMessage = errorResult.get("message").toString();
                    }
                    return Map.of("success", false, "message", errorMessage);
                }
                
                return objectMapper.readValue(responseBody, Map.class);
            });
        } catch (Exception e) {
            return Map.of("success", false, "message", "회원 등록 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    public Map<String, Object> updateMember(Long id, Map<String, Object> memberData) {
        try {
            HttpPut request = new HttpPut(memberServiceUrl + "/api/members/" + id);
            String jsonBody = objectMapper.writeValueAsString(memberData);
            request.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));
            
            return httpClient.execute(request, response -> {
                String responseBody = EntityUtils.toString(response.getEntity());
                
                if (response.getCode() >= 400) {
                    Map<String, Object> errorResult = objectMapper.readValue(responseBody, Map.class);
                    String errorMessage = "회원 수정에 실패했습니다.";
                    if (errorResult.get("message") != null) {
                        errorMessage = errorResult.get("message").toString();
                    }
                    return Map.of("success", false, "message", errorMessage);
                }
                
                return objectMapper.readValue(responseBody, Map.class);
            });
        } catch (Exception e) {
            return Map.of("success", false, "message", "회원 수정 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    public Map<String, Object> deleteMember(Long id) {
        try {
            HttpDelete request = new HttpDelete(memberServiceUrl + "/api/members/" + id);
            
            return httpClient.execute(request, response -> {
                String responseBody = EntityUtils.toString(response.getEntity());
                
                if (response.getCode() >= 400) {
                    if (responseBody.contains("foreign key constraint fails") && responseBody.contains("rental")) {
                        return Map.of("success", false, "message", "대여 기록이 있는 회원은 삭제할 수 없습니다. 먼저 모든 대여를 반납하거나 회원을 비활성화해주세요.", "errorType", "FOREIGN_KEY_CONSTRAINT");
                    }
                    
                    Map<String, Object> errorResult = objectMapper.readValue(responseBody, Map.class);
                    String errorMessage = "회원 삭제에 실패했습니다.";
                    if (errorResult.get("message") != null) {
                        errorMessage = errorResult.get("message").toString();
                    }
                    return Map.of("success", false, "message", errorMessage);
                }
                
                return objectMapper.readValue(responseBody, Map.class);
            });
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("foreign key constraint fails")) {
                return Map.of("success", false, "message", "대여 기록이 있는 회원은 삭제할 수 없습니다. 먼저 모든 대여를 반납하거나 회원을 비활성화해주세요.", "errorType", "FOREIGN_KEY_CONSTRAINT");
            }
            return Map.of("success", false, "message", "회원 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    public Map<String, Object> changeRole(Long id, Map<String, Object> request) {
        try {
            HttpPatch httpPatch = new HttpPatch(memberServiceUrl + "/api/members/" + id + "/role");
            String jsonBody = objectMapper.writeValueAsString(request);
            httpPatch.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));
            
            return httpClient.execute(httpPatch, response -> {
                String responseBody = EntityUtils.toString(response.getEntity());
                
                if (response.getCode() >= 400) {
                    return Map.of("success", false, "message", "권한 변경에 실패했습니다.");
                }
                
                return objectMapper.readValue(responseBody, Map.class);
            });
        } catch (Exception e) {
            return Map.of("success", false, "message", "권한 변경 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    public Map<String, Object> deactivateMember(Long id) {
        try {
            HttpPatch request = new HttpPatch(memberServiceUrl + "/api/members/" + id + "/deactivate");
            
            return httpClient.execute(request, response -> {
                String responseBody = EntityUtils.toString(response.getEntity());
                
                if (response.getCode() >= 400) {
                    return Map.of("success", false, "message", "회원 비활성화에 실패했습니다.");
                }
                
                return objectMapper.readValue(responseBody, Map.class);
            });
        } catch (Exception e) {
            return Map.of("success", false, "message", "회원 비활성화 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    public Map<String, Object> activateMember(Long id) {
        try {
            HttpPatch request = new HttpPatch(memberServiceUrl + "/api/members/" + id + "/activate");
            
            return httpClient.execute(request, response -> {
                String responseBody = EntityUtils.toString(response.getEntity());
                
                if (response.getCode() >= 400) {
                    return Map.of("success", false, "message", "회원 활성화에 실패했습니다.");
                }
                
                return objectMapper.readValue(responseBody, Map.class);
            });
        } catch (Exception e) {
            return Map.of("success", false, "message", "회원 활성화 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    public Map<String, Object> makeAdmin(Long id) {
        try {
            Map<String, Object> updateData = Map.of("role", 1); // ADMIN = 1
            return changeRole(id, updateData);
        } catch (Exception e) {
            return Map.of("success", false, "message", "관리자 권한 부여 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}