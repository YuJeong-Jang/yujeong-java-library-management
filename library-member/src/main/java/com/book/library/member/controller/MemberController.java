package com.book.library.member.controller;

import com.book.library.Enums;
import com.book.library.common.dto.ApiResponse;
import com.book.library.common.exception.BusinessException;
import com.book.library.member.domain.Member;
import com.book.library.member.dto.MemberCreateRequest;
import com.book.library.member.dto.MemberResponse;
import com.book.library.member.dto.MemberUpdateRequest;
import com.book.library.member.repository.MemberRepository;
import com.book.library.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<MemberResponse>>> getAllMembers() {
        List<MemberResponse> members = memberService.getAllMembers();
        return ResponseEntity.ok(ApiResponse.success(members));
    }
    
    @GetMapping("/page")
    public ResponseEntity<ApiResponse<Page<MemberResponse>>> getMembers(Pageable pageable) {
        Page<MemberResponse> members = memberService.getMembers(pageable);
        return ResponseEntity.ok(ApiResponse.success(members));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MemberResponse>> getMember(@PathVariable Long id) {
        MemberResponse member = memberService.getMember(id);
        return ResponseEntity.ok(ApiResponse.success(member));
    }
    
    @GetMapping("/login/{loginId}")
    public ResponseEntity<ApiResponse<MemberResponse>> getMemberByLoginId(@PathVariable String loginId) {
        MemberResponse member = memberService.getMemberByLoginId(loginId);
        return ResponseEntity.ok(ApiResponse.success(member));
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<MemberResponse>> createMember(@Valid @RequestBody MemberCreateRequest request) {
        try {     
            MemberResponse member = memberService.createMember(request);
            return ResponseEntity
                    .created(URI.create("/api/members/" + member.getId()))
                    .body(ApiResponse.success("회원이 성공적으로 등록되었습니다.", member));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("회원 등록 중 오류가 발생했습니다."));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MemberResponse>> updateMember(
            @PathVariable Long id,
            @Valid @RequestBody MemberUpdateRequest request
    ) {
        MemberResponse member = memberService.updateMember(id, request);
        return ResponseEntity.ok(ApiResponse.success("회원 정보가 성공적으로 수정되었습니다.", member));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.ok(ApiResponse.success("회원이 성공적으로 삭제되었습니다.", null));
    }
    
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<Void>> deactivateMember(@PathVariable Long id) {
        memberService.deactivateMember(id);
        return ResponseEntity.ok(ApiResponse.success("회원이 비활성화되었습니다.", null));
    }
    
    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<Void>> activateMember(@PathVariable Long id) {
        memberService.activateMember(id);
        return ResponseEntity.ok(ApiResponse.success("회원이 활성화되었습니다.", null));
    }
    
    @PatchMapping("/{id}/role")
    public ResponseEntity<ApiResponse<Void>> changeRole(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        Integer role = (Integer) request.get("role");
        memberService.changeRole(id, role);
        return ResponseEntity.ok(ApiResponse.success("회원 역할이 변경되었습니다.", null));
    }
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<MemberResponse>> loginMember(@RequestBody Map<String, String> loginRequest) {
        try {
            String loginId = loginRequest.get("loginId");
            String password = loginRequest.get("password");
    
            Member member = memberRepository.findByLoginId(loginId)
                    .orElseThrow(() -> new BusinessException("존재하지 않는 아이디입니다."));
          
            // 비밀번호 확인 (실제로는 암호화된 비밀번호와 비교해야 함)
            if (!password.equals(member.getPassword())) {
                throw new BusinessException("비밀번호가 일치하지 않습니다.");
            }
            
            // 비활성화된 회원 체크
            if (member.getStatus() == Enums.MemberStatus.INACTIVE) {
                throw new BusinessException("비활성화된 계정입니다.");
            }

            return ResponseEntity.ok(ApiResponse.success("로그인에 성공했습니다.", MemberResponse.from(member)));
            
        } catch (BusinessException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("로그인 중 오류가 발생했습니다."));
        }
    }
    
    @PostMapping("/init-test-data")
    public ResponseEntity<ApiResponse<Void>> initTestData() {
        memberService.initializeTestData();
        return ResponseEntity.ok(ApiResponse.success("테스트 데이터가 초기화되었습니다.", null));
    }
}