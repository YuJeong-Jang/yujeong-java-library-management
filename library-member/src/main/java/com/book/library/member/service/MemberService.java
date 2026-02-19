package com.book.library.member.service;

import com.book.library.Enums;
import com.book.library.common.exception.BusinessException;
import com.book.library.member.domain.Member;
import com.book.library.member.dto.MemberCreateRequest;
import com.book.library.member.dto.MemberResponse;
import com.book.library.member.dto.MemberUpdateRequest;
import com.book.library.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
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
        if (memberRepository.findByLoginId(request.getLoginId()).isPresent()) {
            throw new BusinessException("이미 존재하는 로그인 ID입니다: " + request.getLoginId());
        }
        
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BusinessException("이미 존재하는 이메일입니다: " + request.getEmail());
        }
        
        Member member = new Member();
        member.setLoginId(request.getLoginId());
        member.setPassword(request.getPassword());
        member.setName(request.getName());
        member.setEmail(request.getEmail());
        member.setPhone(request.getPhone());
        member.setStatus(Enums.MemberStatus.ACTIVE);
        
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
    public MemberResponse updateMember(Long id, MemberUpdateRequest request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new BusinessException("회원을 찾을 수 없습니다. ID: " + id));
        
        if (!member.getEmail().equals(request.getEmail()) && 
            memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BusinessException("이미 존재하는 이메일입니다: " + request.getEmail());
        }
        
        member.setName(request.getName());
        member.setEmail(request.getEmail());
        member.setPhone(request.getPhone());
        
        // 비밀번호가 제공된 경우에만 업데이트
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            member.setPassword(request.getPassword());
        }
        
        // 권한이 제공된 경우에만 업데이트
        if (request.getRole() != null) {
            if (request.getRole() == 0) {
                member.setRole(Enums.MemberRole.USER);
            } else if (request.getRole() == 1) {
                member.setRole(Enums.MemberRole.ADMIN);
            }
        }
        
        member.setUpdatedAt(Instant.now());
        
        Member saved = memberRepository.save(member);
        return MemberResponse.from(saved);
    }
    
    @Transactional
    public void deleteMember(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new BusinessException("회원을 찾을 수 없습니다. ID: " + id);
        }
        memberRepository.deleteById(id);
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