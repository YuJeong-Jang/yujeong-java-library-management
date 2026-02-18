package com.book.library.member.dto;

import lombok.Data;

@Data
public class MemberUpdateRequest {
    private String name;
    private String email;
    private String phone;
    private String password; // 비밀번호 변경 (선택사항)
}