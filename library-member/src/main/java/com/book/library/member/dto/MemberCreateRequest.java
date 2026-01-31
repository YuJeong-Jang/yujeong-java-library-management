package com.book.library.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MemberCreateRequest {
    @NotBlank(message = "로그인 ID는 필수입니다")
    @Size(min = 4, max = 50, message = "로그인 ID는 4-50자 사이여야 합니다")
    private String loginId;
    
    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다")
    private String password;
    
    @NotBlank(message = "이름은 필수입니다")
    @Size(max = 100, message = "이름은 100자를 초과할 수 없습니다")
    private String name;
    
    @Email(message = "올바른 이메일 형식이 아닙니다")
    @NotBlank(message = "이메일은 필수입니다")
    private String email;
    
    @Size(max = 20, message = "전화번호는 20자를 초과할 수 없습니다")
    private String phone; // @NotBlank 제거 - 선택 필드
    
    private Integer role = 0; // 기본값: 일반 사용자 (0: USER, 1: ADMIN)
}