package com.book.library.frontend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class RentalService {
    
    private final WebClient rentalServiceClient;
    
    public RentalService(@Qualifier("rentalServiceWebClient") WebClient rentalServiceClient) {
        this.rentalServiceClient = rentalServiceClient;
    }
    
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getAllRentals() {
        try {
            // 연체 상태 업데이트 임시 비활성화 (500 에러 발생)
            // updateOverdueStatus();
            
            Map<String, Object> response = rentalServiceClient
                    .get()
                    .uri("/api/rentals")
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
    
    public Map<String, Object> updateOverdueStatus() {
        try {
            Map<String, Object> response = rentalServiceClient
                    .post()
                    .uri("/api/rentals/update-overdue")
                    .retrieve()
                    .bodyToMono(Map.class)
                    .onErrorReturn(Map.of("success", false, "message", "연체 상태 업데이트에 실패했습니다."))
                    .block();
            
            return response != null ? response : Map.of("success", false, "message", "응답이 없습니다.");
        } catch (Exception e) {
            return Map.of("success", false, "message", "연체 상태 업데이트 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    public Map<String, Object> testConnection() {
        try {
            Map<String, Object> response = rentalServiceClient
                    .get()
                    .uri("/api/rentals/test")
                    .retrieve()
                    .bodyToMono(Map.class)
                    .onErrorReturn(Map.of("success", false, "message", "연결 테스트 실패"))
                    .block();
            
            return response != null ? response : Map.of("success", false, "message", "응답이 없습니다.");
        } catch (Exception e) {
            return Map.of("success", false, "message", "연결 테스트 중 오류 발생: " + e.getMessage());
        }
    }
    
    public Map<String, Object> createRental(Map<String, Object> rentalData) {
        try {
            // 요청 데이터 형식 확인 및 변환
            Map<String, Object> requestData = Map.of(
                "memberId", rentalData.get("memberId"),
                "bookId", rentalData.get("bookId"),
                "remarks", rentalData.getOrDefault("remarks", "")
            );
            
            Map<String, Object> response = rentalServiceClient
                    .post()
                    .uri("/api/rentals")
                    .header("Content-Type", "application/json")
                    .bodyValue(requestData)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), clientResponse -> {
                        return clientResponse.bodyToMono(Map.class)
                                .map(errorBody -> {
                                    String errorMessage = "대여 등록에 실패했습니다.";
                                    if (errorBody != null && errorBody.get("message") != null) {
                                        errorMessage = errorBody.get("message").toString();
                                    }
                                    return new RuntimeException("API_ERROR:" + errorMessage);
                                });
                    })
                    .bodyToMono(Map.class)
                    .onErrorResume(throwable -> {
                        String errorMessage = "대여 등록 중 오류가 발생했습니다.";
                        if (throwable.getMessage() != null && throwable.getMessage().startsWith("API_ERROR:")) {
                            errorMessage = throwable.getMessage().substring("API_ERROR:".length());
                        }
                        return reactor.core.publisher.Mono.just(Map.of("success", false, "message", errorMessage));
                    })
                    .block();
            
            return response != null ? response : Map.of("success", false, "message", "응답이 없습니다.");
        } catch (Exception e) {
            return Map.of("success", false, "message", "대여 등록 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    public Map<String, Object> returnBook(Long id) {
        try {
            Map<String, Object> response = rentalServiceClient
                    .patch()
                    .uri("/api/rentals/" + id + "/return")
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), clientResponse -> {
                        return clientResponse.bodyToMono(Map.class)
                                .map(errorBody -> {
                                    String errorMessage = "도서 반납에 실패했습니다.";
                                    if (errorBody != null && errorBody.get("message") != null) {
                                        errorMessage = errorBody.get("message").toString();
                                    }
                                    return new RuntimeException("API_ERROR:" + errorMessage);
                                });
                    })
                    .bodyToMono(Map.class)
                    .onErrorResume(throwable -> {
                        String errorMessage = "도서 반납 중 오류가 발생했습니다.";
                        if (throwable.getMessage() != null && throwable.getMessage().startsWith("API_ERROR:")) {
                            errorMessage = throwable.getMessage().substring("API_ERROR:".length());
                        }
                        return reactor.core.publisher.Mono.just(Map.of("success", false, "message", errorMessage));
                    })
                    .block();
            
            return response != null ? response : Map.of("success", false, "message", "응답이 없습니다.");
        } catch (Exception e) {
            return Map.of("success", false, "message", "도서 반납 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    public Map<String, Object> deleteRental(Long id) {
        try {
            Map<String, Object> response = rentalServiceClient
                    .delete()
                    .uri("/api/rentals/" + id)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), clientResponse -> {
                        return clientResponse.bodyToMono(Map.class)
                                .map(errorBody -> {
                                    String errorMessage = "대여 기록 삭제에 실패했습니다.";
                                    if (errorBody != null && errorBody.get("message") != null) {
                                        errorMessage = errorBody.get("message").toString();
                                    }
                                    return new RuntimeException("API_ERROR:" + errorMessage);
                                });
                    })
                    .bodyToMono(Map.class)
                    .onErrorResume(throwable -> {
                        String errorMessage = "대여 기록 삭제 중 오류가 발생했습니다.";
                        if (throwable.getMessage() != null && throwable.getMessage().startsWith("API_ERROR:")) {
                            errorMessage = throwable.getMessage().substring("API_ERROR:".length());
                        }
                        return reactor.core.publisher.Mono.just(Map.of("success", false, "message", errorMessage));
                    })
                    .block();
            
            return response != null ? response : Map.of("success", false, "message", "응답이 없습니다.");
        } catch (Exception e) {
            return Map.of("success", false, "message", "대여 기록 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}