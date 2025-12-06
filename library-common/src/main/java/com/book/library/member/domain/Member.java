package com.book.library.member.domain;

import com.book.library.rental.domain.Rental;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

enum MemberStatus { ACTIVE, INACTIVE } // active = 0, inactive = 1
enum MemberRole { USER, ADMIN } // user = 0, admin = 1

@Entity
@Table(name = "member")
@Data
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    private Long id;

    @Size(max = 50)
    @NotNull
    @Column(name = "login_id", nullable = false, length = 50)
    private String loginId;

    @Size(max = 255)
    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @Size(max = 100)
    @NotNull
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Size(max = 100)
    @NotNull
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Size(max = 20)
    @Column(name = "phone", length = 20)
    private String phone;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private MemberStatus status = MemberStatus.ACTIVE;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "role")
    private MemberRole role = MemberRole.USER;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "member")
    private Set<Rental> rentals = new LinkedHashSet<>();

}