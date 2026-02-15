package com.book.library.frontend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookService {
    
    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    @Qualifier("bookServiceUrl")
    private final String bookServiceUrl;
    
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getAllBooks() {
        try {
            HttpGet request = new HttpGet(bookServiceUrl + "/api/books");
            
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
    public List<Map<String, Object>> searchBooks(String keyword) {
        try {
            String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
            HttpGet request = new HttpGet(bookServiceUrl + "/api/books/search?keyword=" + encodedKeyword);
            
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
    public Map<String, Object> getBook(Long id) {
        try {
            HttpGet request = new HttpGet(bookServiceUrl + "/api/books/" + id);
            
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
    
    public Map<String, Object> createBook(Map<String, Object> bookData) {
        try {
            HttpPost request = new HttpPost(bookServiceUrl + "/api/books");
            String jsonBody = objectMapper.writeValueAsString(bookData);
            request.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));
            
            return httpClient.execute(request, response -> {
                String responseBody = EntityUtils.toString(response.getEntity());
                
                if (response.getCode() >= 400) {
                    Map<String, Object> errorResult = objectMapper.readValue(responseBody, Map.class);
                    String errorMessage = "도서 등록에 실패했습니다.";
                    if (errorResult.get("message") != null) {
                        errorMessage = errorResult.get("message").toString();
                    }
                    return Map.of("success", false, "message", errorMessage);
                }
                
                return objectMapper.readValue(responseBody, Map.class);
            });
        } catch (Exception e) {
            return Map.of("success", false, "message", "도서 등록 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    public Map<String, Object> updateBook(Long id, Map<String, Object> bookData) {
        try {
            HttpPut request = new HttpPut(bookServiceUrl + "/api/books/" + id);
            String jsonBody = objectMapper.writeValueAsString(bookData);
            request.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));
            
            return httpClient.execute(request, response -> {
                String responseBody = EntityUtils.toString(response.getEntity());
                
                if (response.getCode() >= 400) {
                    Map<String, Object> errorResult = objectMapper.readValue(responseBody, Map.class);
                    String errorMessage = "도서 수정에 실패했습니다.";
                    if (errorResult.get("message") != null) {
                        errorMessage = errorResult.get("message").toString();
                    }
                    return Map.of("success", false, "message", errorMessage);
                }
                
                return objectMapper.readValue(responseBody, Map.class);
            });
        } catch (Exception e) {
            return Map.of("success", false, "message", "도서 수정 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    public Map<String, Object> deleteBook(Long id) {
        try {
            HttpDelete request = new HttpDelete(bookServiceUrl + "/api/books/" + id);
            
            return httpClient.execute(request, response -> {
                String responseBody = EntityUtils.toString(response.getEntity());
                
                if (response.getCode() >= 400) {
                    Map<String, Object> errorResult = objectMapper.readValue(responseBody, Map.class);
                    String errorMessage = "도서 삭제에 실패했습니다.";
                    if (errorResult.get("message") != null) {
                        errorMessage = errorResult.get("message").toString();
                    }
                    return Map.of("success", false, "message", errorMessage);
                }
                
                return objectMapper.readValue(responseBody, Map.class);
            });
        } catch (Exception e) {
            return Map.of("success", false, "message", "도서 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}