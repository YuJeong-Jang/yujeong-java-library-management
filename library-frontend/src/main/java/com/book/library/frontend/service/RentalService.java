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
public class RentalService {
    
    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    @Qualifier("rentalServiceUrl")
    private final String rentalServiceUrl;
    
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getAllRentals() {
        try {
            HttpGet request = new HttpGet(rentalServiceUrl + "/api/rentals");
            
            return httpClient.execute(request, response -> {
                String responseBody = EntityUtils.toString(response.getEntity());
                Map<String, Object> result = objectMapper.readValue(responseBody, Map.class);
                
                if (Boolean.TRUE.equals(result.get("success"))) {
                    return (List<Map<String, Object>>) result.get("data");
                }
                return List.of();
            });
        } catch (Exception e) {
            return List.of();
        }
    }
    
    @SuppressWarnings("unchecked")
    public Map<String, Object> getRental(Long id) {
        try {
            HttpGet request = new HttpGet(rentalServiceUrl + "/api/rentals/" + id);
            
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
    
    public Map<String, Object> createRental(Map<String, Object> rentalData) {
        try {
            HttpPost request = new HttpPost(rentalServiceUrl + "/api/rentals");
            String jsonBody = objectMapper.writeValueAsString(rentalData);
            request.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));
            
            return httpClient.execute(request, response -> {
                String responseBody = EntityUtils.toString(response.getEntity());
                
                if (response.getCode() >= 400) {
                    Map<String, Object> errorResult = objectMapper.readValue(responseBody, Map.class);
                    String errorMessage = "대여 등록에 실패했습니다.";
                    if (errorResult.get("message") != null) {
                        errorMessage = errorResult.get("message").toString();
                    }
                    return Map.of("success", false, "message", errorMessage);
                }
                
                return objectMapper.readValue(responseBody, Map.class);
            });
        } catch (Exception e) {
            return Map.of("success", false, "message", "대여 등록 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    public Map<String, Object> returnBook(Long rentalId) {
        try {
            HttpPatch request = new HttpPatch(rentalServiceUrl + "/api/rentals/" + rentalId + "/return");
            
            return httpClient.execute(request, response -> {
                String responseBody = EntityUtils.toString(response.getEntity());
                
                if (response.getCode() >= 400) {
                    Map<String, Object> errorResult = objectMapper.readValue(responseBody, Map.class);
                    String errorMessage = "도서 반납에 실패했습니다.";
                    if (errorResult.get("message") != null) {
                        errorMessage = errorResult.get("message").toString();
                    }
                    return Map.of("success", false, "message", errorMessage);
                }
                
                return objectMapper.readValue(responseBody, Map.class);
            });
        } catch (Exception e) {
            return Map.of("success", false, "message", "도서 반납 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    public Map<String, Object> extendRental(Long rentalId) {
        try {
            HttpPatch request = new HttpPatch(rentalServiceUrl + "/api/rentals/" + rentalId + "/extend");
            
            return httpClient.execute(request, response -> {
                String responseBody = EntityUtils.toString(response.getEntity());
                
                if (response.getCode() >= 400) {
                    Map<String, Object> errorResult = objectMapper.readValue(responseBody, Map.class);
                    String errorMessage = "대여 연장에 실패했습니다.";
                    if (errorResult.get("message") != null) {
                        errorMessage = errorResult.get("message").toString();
                    }
                    return Map.of("success", false, "message", errorMessage);
                }
                
                return objectMapper.readValue(responseBody, Map.class);
            });
        } catch (Exception e) {
            return Map.of("success", false, "message", "대여 연장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    public Map<String, Object> deleteRental(Long rentalId) {
        try {
            HttpDelete request = new HttpDelete(rentalServiceUrl + "/api/rentals/" + rentalId);
            
            return httpClient.execute(request, response -> {
                String responseBody = EntityUtils.toString(response.getEntity());
                
                if (response.getCode() >= 400) {
                    Map<String, Object> errorResult = objectMapper.readValue(responseBody, Map.class);
                    String errorMessage = "대여 삭제에 실패했습니다.";
                    if (errorResult.get("message") != null) {
                        errorMessage = errorResult.get("message").toString();
                    }
                    return Map.of("success", false, "message", errorMessage);
                }
                
                return objectMapper.readValue(responseBody, Map.class);
            });
        } catch (Exception e) {
            return Map.of("success", false, "message", "대여 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    public void updateOverdueStatus() {
        try {
            HttpPost request = new HttpPost(rentalServiceUrl + "/api/rentals/update-overdue");
            httpClient.execute(request, response -> {
                // 응답 처리 (필요시)
                return null;
            });
        } catch (Exception e) {
            // 연체 상태 업데이트 실패는 무시 (선택적 기능)
        }
    }
    
    public Map<String, Object> testConnection() {
        try {
            HttpGet request = new HttpGet(rentalServiceUrl + "/api/rentals");
            
            return httpClient.execute(request, response -> {
                if (response.getCode() == 200) {
                    return Map.of("success", true, "message", "대여 서비스 연결 성공", "url", rentalServiceUrl);
                } else {
                    return Map.of("success", false, "message", "대여 서비스 연결 실패: " + response.getCode(), "url", rentalServiceUrl);
                }
            });
        } catch (Exception e) {
            return Map.of("success", false, "message", "대여 서비스 연결 오류: " + e.getMessage(), "url", rentalServiceUrl);
        }
    }
}