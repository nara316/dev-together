package com.project.devtogether.member.domain;

import com.project.devtogether.member.domain.enums.MemberRole;
import com.project.devtogether.member.domain.enums.MemberStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@ToString
@Entity
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String email;

    @Setter
    @Column(length = 50, nullable = false)
    private String password;

    @Column(length = 25, nullable = false)
    private String name;

    @Setter
    @Column(length = 10)
    private String nickName;

    @Setter
    @Column(length = 500)
    private String introduce;

    @Column(length = 25, nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Column(length = 25, nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createAt;


    private LocalDateTime lastLoginAt;
}
