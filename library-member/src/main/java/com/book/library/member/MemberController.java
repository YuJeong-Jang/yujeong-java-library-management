package com.book.library.member;

import com.book.library.member.domain.Member;
import com.book.library.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/library/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;

    @GetMapping
    public List<Member> getAllMember() { return memberRepository.findAll(); }

    @PostMapping
    public Member createMember(@RequestBody Member member) { return memberRepository.save(member); }
}
