package com.book.library.member.service;

import com.book.library.Enums;
import com.book.library.common.exception.BusinessException;
import com.book.library.member.domain.Member;
import com.book.library.member.dto.MemberCreateRequest;
import com.book.library.member.dto.MemberResponse;
import com.book.library.member.dto.MemberUpdateRequest;
import com.book.library.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    
    private final MemberRepository memberRepository;
    
    public List<MemberResponse> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(MemberResponse::from)
                .toList();
    }
    
    public Page<MemberResponse> getMembers(Pageable pageable) {
        return memberRepository.findAll(pageable)
                .map(MemberResponse::from);
    }
    
    public MemberResponse getMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new BusinessException("회원을 찾을 수 없습니다. ID: " + id));
        return MemberResponse.from(member);
    }
    
    public MemberResponse getMemberByLoginId(String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new BusinessException("회원을 찾을 수 없습니다. 로그인 ID: " + loginId));
        return MemberResponse.from(member);
    }
    
    @Transactional
    public MemberResponse createMember(MemberCreateRequest request) {
        // 중복 체크
        if (memberRepository.existsByLoginId(request.getLoginId())) {
            throw new BusinessException("이미 존재하는 로그인 ID입니다: " + request.getLoginId());
        }
        
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("이미 존재하는 이메일입니다: " + request.getEmail());
        }
        
        Member member = new Member();
        member.setLoginId(request.getLoginId());
        member.setPassword(request.getPassword()); // 실제로는 암호화 필요
        member.setName(request.getName());
        member.setEmail(request.getEmail());
        member.setPhone(request.getPhone());
        member.setStatus(Enums.MemberStatus.ACTIVE);
        
        // 역할 설정
        if (request.getRole() != null && request.getRole() == 1) {
            member.setRole(Enums.MemberRole.ADMIN);
        } else {
            member.setRole(Enums.MemberRole.USER);
        }
        
        member.setCreatedAt(Instant.now());
        member.setUpdatedAt(Instant.now());
        
        Member saved = memberRepository.save(member);
        return MemberResponse.from(saved);
    }
    
    @Transactional
    public void initializeTestData() {
        // 관리자 계정이 없으면 생성
        if (!memberRepository.existsByLoginId("admin")) {
            Member admin = new Member();
            admin.setLoginId("admin");
            admin.setPassword("admin123");
            admin.setName("관리자");
            admin.setEmail("admin@library.com");
            admin.setPhone("010-0000-0000");
            admin.setStatus(Enums.MemberStatus.ACTIVE);
            admin.setRole(Enums.MemberRole.ADMIN);
            admin.setCreatedAt(Instant.now());
            admin.setUpdatedAt(Instant.now());
            memberRepository.save(admin);
        }
        
        // 테스트 사용자가 없으면 생성
        if (!memberRepository.existsByLoginId("user1")) {
            Member user = new Member();
            user.setLoginId("user1");
            user.setPassword("user123");
            user.setName("홍길동");
            user.setEmail("user1@library.com");
            user.setPhone("010-1111-1111");
            user.setStatus(Enums.MemberStatus.ACTIVE);
            user.setRole(Enums.MemberRole.USER);
            user.setCreatedAt(Instant.now());
            user.setUpdatedAt(Instant.now());
            memberRepository.save(user);
        }
    }
    
    @Transactional
    public MemberResponse updateMember(Long id, MemberUpdateRequest request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new BusinessException("회원을 찾을 수 없습니다. ID: " + id));
        
        // 이메일 중복 체크 (자신 제외)
        if (!member.getEmail().equals(request.getEmail()) && 
            memberRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("이미 존재하는 이메일입니다: " + request.getEmail());
        }
        
        member.setName(request.getName());
        member.setEmail(request.getEmail());
        member.setPhone(request.getPhone());
        member.setUpdatedAt(Instant.now());
        
        Member saved = memberRepository.save(member);
        return MemberResponse.from(saved);
    }
    
    @Transactional
    public void deleteMember(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new BusinessException("회원을 찾을 수 없습니다. ID: " + id);
        }
        
        try {
            memberRepository.deleteById(id);
        } catch (Exception e) {
            // 외래키 제약조건 오류 체크
            if (e.getMessage() != null && e.getMessage().contains("foreign key constraint fails")) {
                if (e.getMessage().contains("rental")) {
                    throw new BusinessException("대여 기록이 있는 회원은 삭제할 수 없습니다. 먼저 모든 대여를 반납하거나 회원을 비활성화해주세요.");
                } else {
                    throw new BusinessException("다른 데이터와 연결되어 있어 삭제할 수 없습니다. 먼저 관련 데이터를 정리해주세요.");
                }
            }
            
            throw new BusinessException("회원 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    @Transactional
    public void deactivateMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new BusinessException("회원을 찾을 수 없습니다. ID: " + id));
        
        member.setStatus(Enums.MemberStatus.INACTIVE);
        member.setUpdatedAt(Instant.now());
        memberRepository.save(member);
    }
    
    @Transactional
    public void activateMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new BusinessException("회원을 찾을 수 없습니다. ID: " + id));
        
        member.setStatus(Enums.MemberStatus.ACTIVE);
        member.setUpdatedAt(Instant.now());
        memberRepository.save(member);
    }
    
    @Transactional
    public void changeRole(Long id, Integer role) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new BusinessException("회원을 찾을 수 없습니다. ID: " + id));
        
        if (role == 0) {
            member.setRole(Enums.MemberRole.USER);
        } else if (role == 1) {
            member.setRole(Enums.MemberRole.ADMIN);
        } else {
            throw new BusinessException("잘못된 역할 값입니다: " + role);
        }
        
        member.setUpdatedAt(Instant.now());
        memberRepository.save(member);
    }
}