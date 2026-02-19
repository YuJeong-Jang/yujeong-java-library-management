package com.book.library.member.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberUpdateRequest {
    private String name;
    private String email;
    private String phone;
    private String password; // 비밀번호 변경 (선택사항)
    private Integer role; // 권한 변경 (선택사항)
}