package com.book.library.member.repository;

import com.book.library.Enums;
import com.book.library.member.domain.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepository {
    
    private final JdbcTemplate jdbcTemplate;
    
    public MemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    private final RowMapper<Member> rowMapper = (rs, rowNum) -> {
        Member member = new Member();
        member.setId(rs.getLong("member_id"));
        member.setLoginId(rs.getString("login_id"));
        member.setPassword(rs.getString("password"));
        member.setName(rs.getString("name"));
        member.setEmail(rs.getString("email"));
        member.setPhone(rs.getString("phone"));
        member.setStatus(Enums.MemberStatus.values()[rs.getInt("status")]);
        member.setRole(Enums.MemberRole.values()[rs.getInt("role")]);
        member.setCreatedAt(rs.getTimestamp("created_at").toInstant());
        member.setUpdatedAt(rs.getTimestamp("updated_at").toInstant());
        return member;
    };
    
    public List<Member> findAll() {
        return jdbcTemplate.query("SELECT * FROM member", rowMapper);
    }
    
    public Optional<Member> findById(Long id) {
        List<Member> results = jdbcTemplate.query(
            "SELECT * FROM member WHERE member_id = ?", rowMapper, id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
    
    public Optional<Member> findByLoginId(String loginId) {
        List<Member> results = jdbcTemplate.query(
            "SELECT * FROM member WHERE login_id = ?", rowMapper, loginId);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
    
    public Optional<Member> findByEmail(String email) {
        List<Member> results = jdbcTemplate.query(
            "SELECT * FROM member WHERE email = ?", rowMapper, email);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
    
    public Member save(Member member) {
        if (member.getId() == null) {
            return insert(member);
        } else {
            update(member);
            return member;
        }
    }
    
    private Member insert(Member member) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        Instant now = Instant.now();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO member (login_id, password, name, email, phone, status, role, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, member.getLoginId());
            ps.setString(2, member.getPassword());
            ps.setString(3, member.getName());
            ps.setString(4, member.getEmail());
            ps.setString(5, member.getPhone());
            ps.setInt(6, member.getStatus().ordinal());
            ps.setInt(7, member.getRole().ordinal());
            ps.setTimestamp(8, java.sql.Timestamp.from(now));
            ps.setTimestamp(9, java.sql.Timestamp.from(now));
            return ps;
        }, keyHolder);
        
        member.setId(keyHolder.getKey().longValue());
        member.setCreatedAt(now);
        member.setUpdatedAt(now);
        return member;
    }
    
    private void update(Member member) {
        Instant now = Instant.now();
        jdbcTemplate.update(
            "UPDATE member SET login_id=?, password=?, name=?, email=?, phone=?, status=?, role=?, updated_at=? " +
            "WHERE member_id=?",
            member.getLoginId(), member.getPassword(), member.getName(), member.getEmail(),
            member.getPhone(), member.getStatus().ordinal(), member.getRole().ordinal(),
            java.sql.Timestamp.from(now), member.getId());
        member.setUpdatedAt(now);
    }
    
    public boolean existsById(Long id) {
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM member WHERE member_id = ?", Integer.class, id);
        return count != null && count > 0;
    }
    
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM member WHERE member_id = ?", id);
    }
}
