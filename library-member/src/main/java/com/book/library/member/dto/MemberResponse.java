package com.book.library.member.dto;

import com.book.library.Enums;
import com.book.library.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {
    private Long id;
    private String loginId;
    private String name;
    private String email;
    private String phone;
    private Enums.MemberStatus status;
    private Enums.MemberRole role;
    private Instant createdAt;
    private Instant updatedAt;
    
    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getLoginId(),
                member.getName(),
                member.getEmail(),
                member.getPhone(),
                member.getStatus(),
                member.getRole(),
                member.getCreatedAt(),
                member.getUpdatedAt()
        );
    }
}