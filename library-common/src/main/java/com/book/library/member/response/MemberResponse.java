package com.book.library.member.response;

import com.book.library.Enums;
import com.book.library.member.domain.Member;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class MemberResponse {
    Long id;
    String loginId;
    String name;
    String email;
    String phone;
    Enums.MemberStatus status;   // ACTIVE / INACTIVE
    Enums.MemberRole role;     // USER / ADMIN
    Instant createdAt;
    Instant updatedAt;

    public static MemberResponse from(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .loginId(member.getLoginId())
                .name(member.getName())
                .email(member.getEmail())
                .phone(member.getPhone())
                .status(member.getStatus())
                .role(member.getRole())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }
}
