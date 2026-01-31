package com.book.library.frontend.controller;

import com.book.library.frontend.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    
    private final MemberService memberService;
    
    @GetMapping
    public String members(Model model, HttpSession session) {
        // 로그인 정보 추가
        Map<String, Object> loginMember = (Map<String, Object>) session.getAttribute("loginMember");
        model.addAttribute("loginMember", loginMember);
        
        try {
            List<Map<String, Object>> members = memberService.getAllMembers();
            model.addAttribute("members", members);
            
            // 통계 계산 - 더 견고한 로직
            long totalMembers = members != null ? members.size() : 0;
            
            long activeMembers = 0;
            long inactiveMembers = 0;
            long adminMembers = 0;
            
            if (members != null) {
                for (Map<String, Object> member : members) {
                    // 상태 확인 (다양한 형태의 데이터 처리)
                    Object status = member.get("status");
                    if (status != null) {
                        String statusStr = status.toString().toUpperCase();
                        if ("ACTIVE".equals(statusStr) || "0".equals(statusStr) || "활성".equals(statusStr)) {
                            activeMembers++;
                        } else if ("INACTIVE".equals(statusStr) || "1".equals(statusStr) || "비활성".equals(statusStr)) {
                            inactiveMembers++;
                        }
                    }
                    
                    // 역할 확인 (다양한 형태의 데이터 처리)
                    Object role = member.get("role");
                    if (role != null) {
                        String roleStr = role.toString().toUpperCase();
                        if ("ADMIN".equals(roleStr) || "1".equals(roleStr) || "관리자".equals(roleStr)) {
                            adminMembers++;
                        }
                    }
                }
            }
            
            // 디버깅용 로그
            model.addAttribute("totalMembers", totalMembers);
            model.addAttribute("activeMembers", activeMembers);
            model.addAttribute("inactiveMembers", inactiveMembers);
            model.addAttribute("adminMembers", adminMembers);
            
        } catch (Exception e) {
            model.addAttribute("members", java.util.Collections.emptyList());
            model.addAttribute("error", "회원 목록을 불러오는 중 오류가 발생했습니다: " + e.getMessage());
            model.addAttribute("totalMembers", 0);
            model.addAttribute("activeMembers", 0);
            model.addAttribute("inactiveMembers", 0);
            model.addAttribute("adminMembers", 0);
        }
        return "members/members";
    }
    
    @PostMapping
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createMember(@RequestBody Map<String, Object> memberData) {
        Map<String, Object> result = memberService.createMember(memberData);
        return ResponseEntity.ok(result);
    }
    
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateMember(@PathVariable Long id, @RequestBody Map<String, Object> memberData) {
        Map<String, Object> result = memberService.updateMember(id, memberData);
        return ResponseEntity.ok(result);
    }
    
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteMember(@PathVariable Long id) {
        Map<String, Object> result = memberService.deleteMember(id);
        return ResponseEntity.ok(result);
    }
    
    @PatchMapping("/{id}/deactivate")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deactivateMember(@PathVariable Long id) {
        Map<String, Object> result = memberService.deactivateMember(id);
        return ResponseEntity.ok(result);
    }
    
    @PatchMapping("/{id}/activate")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> activateMember(@PathVariable Long id) {
        Map<String, Object> result = memberService.activateMember(id);
        return ResponseEntity.ok(result);
    }
    
    @PatchMapping("/{id}/role")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> changeRole(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        Map<String, Object> result = memberService.changeRole(id, request);
        return ResponseEntity.ok(result);
    }
    
    // API 엔드포인트
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getAllMembersApi() {
        try {
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", memberService.getAllMembers()
            ));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of(
                "success", false,
                "message", "회원 목록을 불러오는 중 오류가 발생했습니다."
            ));
        }
    }
    
    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getMemberApi(@PathVariable Long id) {
        try {
            Map<String, Object> member = memberService.getMember(id);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", member
            ));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of(
                "success", false,
                "message", "회원 정보를 불러오는 중 오류가 발생했습니다."
            ));
        }
    }
}