package com.book.library.member.dto;

import lombok.Data;

@Data
public class MemberCreateRequest {
    private String loginId;
    private String password;
    private String name;
    private String email;
    private String phone;
    private Integer role = 0;
}