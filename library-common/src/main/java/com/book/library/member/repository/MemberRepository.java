package com.book.library.member.repository;

import com.book.library.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("SELECT m FROM Member m WHERE m.loginId = :loginId")
    Optional<Member> findByLoginId(@Param("loginId") String loginId);
    
    @Query("SELECT m FROM Member m WHERE m.email = :email")
    Optional<Member> findByEmail(@Param("email") String email);
    
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Member m WHERE m.loginId = :loginId")
    boolean existsByLoginId(@Param("loginId") String loginId);
    
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Member m WHERE m.email = :email")
    boolean existsByEmail(@Param("email") String email);
}
