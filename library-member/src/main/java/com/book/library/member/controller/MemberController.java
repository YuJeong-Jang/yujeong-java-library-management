package com.book.library.member.controller;

import com.book.library.member.domain.Member;
import com.book.library.member.repository.MemberRepository;
import com.book.library.member.response.MemberResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/library/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;

    // 1) 전체 목록 조회 (GET /library/members)
    @GetMapping
    public List<MemberResponse> getMembers() {
        return memberRepository.findAll().stream()
                .map(MemberResponse::from)
                .toList();
    }

    // 2) 단건 조회 (GET /library/members/{id})
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getMember(@PathVariable Long id) {
        return memberRepository.findById(id)
                .map(MemberResponse::from)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 3) 생성 (POST /library/members)
    @PostMapping
    public ResponseEntity<MemberResponse> createMember(@Valid @RequestBody Member request) {
        Member saved = memberRepository.save(request);
        return ResponseEntity
                .created(URI.create("/library/members/" + saved.getId()))
                .body(MemberResponse.from(saved));
    }

    // 4) 수정 (PUT /library/members/{id})
    @PutMapping("/{id}")
    public ResponseEntity<MemberResponse> updateMember(
            @PathVariable Long id,
            @Valid @RequestBody Member request
    ) {
        return memberRepository.findById(id)
                .map(existing -> {
                    existing.setLoginId(request.getLoginId());
                    existing.setPassword(request.getPassword());
                    existing.setName(request.getName());
                    existing.setEmail(request.getEmail());
                    existing.setPhone(request.getPhone());
                    existing.setStatus(request.getStatus());
                    existing.setRole(request.getRole());
                    // createdAt은 그대로 두고 updatedAt만 갱신하는 식으로 수정 가능
                    existing.setUpdatedAt(request.getUpdatedAt());
                    Member saved = memberRepository.save(existing);
                    return ResponseEntity.ok(MemberResponse.from(saved));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 5) 삭제 (DELETE /library/members/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        if (!memberRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        memberRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
