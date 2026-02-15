package com.book.library.member.domain;

import com.book.library.Enums;
import lombok.Data;

import java.time.Instant;

@Data
public class Member {
    private Long id;
    private String loginId;
    private String password;
    private String name;
    private String email;
    private String phone;
    private Enums.MemberStatus status = Enums.MemberStatus.ACTIVE;
    private Enums.MemberRole role = Enums.MemberRole.USER;
    private Instant createdAt;
    private Instant updatedAt;
}