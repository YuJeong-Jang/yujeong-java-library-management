package com.book.library.member.dto;

import lombok.Data;

@Data
public class MemberUpdateRequest {
    private String name;
    private String email;
    private String phone;
}