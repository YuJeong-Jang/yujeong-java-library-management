package com.book.library.frontend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class BookService {
    
    private final WebClient bookServiceClient;
    
    public BookService(@Qualifier("bookServiceWebClient") WebClient bookServiceClient) {
        this.bookServiceClient = bookServiceClient;
    }
    
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getAllBooks() {
        try {
            Map<String, Object> response = bookServiceClient
                    .get()
                    .uri("/api/books")
                    .retrieve()
                    .bodyToMono(Map.class)
                    .onErrorReturn(Map.of("success", false, "data", List.of()))
                    .block();
            
            if (response != null && Boolean.TRUE.equals(response.get("success"))) {
                return (List<Map<String, Object>>) response.get("data");
            }
            return List.of();
        } catch (Exception e) {
            return List.of();
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> searchBooks(String keyword) {
        try {
            Map<String, Object> response = bookServiceClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/books/search")
                            .queryParam("keyword", keyword)
                            .build())
                    .retrieve()
                    .bodyToMono(Map.class)
                    .onErrorReturn(Map.of("success", false, "data", List.of()))
                    .block();
            
            if (response != null && Boolean.TRUE.equals(response.get("success"))) {
                return (List<Map<String, Object>>) response.get("data");
            }
            return List.of();
        } catch (Exception e) {
            return List.of();
        }
    }
    
    @SuppressWarnings("unchecked")
    public Map<String, Object> getBook(Long id) {
        try {
            Map<String, Object> response = bookServiceClient
                    .get()
                    .uri("/api/books/" + id)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .onErrorReturn(Map.of("success", false, "message", "도서를 찾을 수 없습니다."))
                    .block();
            
            if (response != null && Boolean.TRUE.equals(response.get("success"))) {
                return (Map<String, Object>) response.get("data");
            }
            return Map.of();
        } catch (Exception e) {
            return Map.of();
        }
    }
    
    public Map<String, Object> createBook(Map<String, Object> bookData) {
        try {
            Map<String, Object> response = bookServiceClient
                    .post()
                    .uri("/api/books")
                    .bodyValue(bookData)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), clientResponse -> {
                        return clientResponse.bodyToMono(Map.class)
                                .map(errorBody -> {
                                    String errorMessage = "도서 등록에 실패했습니다.";
                                    if (errorBody != null && errorBody.get("message") != null) {
                                        errorMessage = errorBody.get("message").toString();
                                    }
                                    return new RuntimeException("API_ERROR:" + errorMessage);
                                });
                    })
                    .bodyToMono(Map.class)
                    .onErrorResume(throwable -> {
                        String errorMessage = "도서 등록에 실패했습니다.";
                        if (throwable.getMessage() != null && throwable.getMessage().startsWith("API_ERROR:")) {
                            errorMessage = throwable.getMessage().substring("API_ERROR:".length());
                        }
                        return Mono.just(Map.of("success", false, "message", errorMessage));
                    })
                    .block();
            
            return response != null ? response : Map.of("success", false, "message", "응답이 없습니다.");
        } catch (Exception e) {
            return Map.of("success", false, "message", "도서 등록 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    public Map<String, Object> updateBook(Long id, Map<String, Object> bookData) {
        try {
            Map<String, Object> response = bookServiceClient
                    .put()
                    .uri("/api/books/" + id)
                    .bodyValue(bookData)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), clientResponse -> {
                        return clientResponse.bodyToMono(Map.class)
                                .map(errorBody -> {
                                    String errorMessage = "도서 수정에 실패했습니다.";
                                    if (errorBody != null && errorBody.get("message") != null) {
                                        errorMessage = errorBody.get("message").toString();
                                    }
                                    return new RuntimeException("API_ERROR:" + errorMessage);
                                });
                    })
                    .bodyToMono(Map.class)
                    .onErrorResume(throwable -> {
                        String errorMessage = "도서 수정에 실패했습니다.";
                        if (throwable.getMessage() != null && throwable.getMessage().startsWith("API_ERROR:")) {
                            errorMessage = throwable.getMessage().substring("API_ERROR:".length());
                        }
                        return Mono.just(Map.of("success", false, "message", errorMessage));
                    })
                    .block();
            
            return response != null ? response : Map.of("success", false, "message", "응답이 없습니다.");
        } catch (Exception e) {
            return Map.of("success", false, "message", "도서 수정 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    public Map<String, Object> deleteBook(Long id) {
        try {
            Map<String, Object> response = bookServiceClient
                    .delete()
                    .uri("/api/books/" + id)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), clientResponse -> {
                        return clientResponse.bodyToMono(Map.class)
                                .map(errorBody -> {
                                    String errorMessage = "도서 삭제에 실패했습니다.";
                                    if (errorBody != null && errorBody.get("message") != null) {
                                        errorMessage = errorBody.get("message").toString();
                                    }
                                    return new RuntimeException("API_ERROR:" + errorMessage);
                                });
                    })
                    .bodyToMono(Map.class)
                    .onErrorResume(throwable -> {
                        String errorMessage = "도서 삭제에 실패했습니다.";
                        if (throwable.getMessage() != null && throwable.getMessage().startsWith("API_ERROR:")) {
                            errorMessage = throwable.getMessage().substring("API_ERROR:".length());
                        }
                        return Mono.just(Map.of("success", false, "message", errorMessage));
                    })
                    .block();
            
            return response != null ? response : Map.of("success", false, "message", "응답이 없습니다.");
        } catch (Exception e) {
            return Map.of("success", false, "message", "도서 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}