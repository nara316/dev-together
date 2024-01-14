package com.project.devtogether.project.domain;

import com.project.devtogether.member.domain.Member;
import com.project.devtogether.project.domain.enums.ProjectStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@Entity
public class Project {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(length = 100, nullable = false)
    private String title;

    @Setter
    @Column(length = 500, nullable = false)
    private String content;

    @Setter
    @Column(length = 25, nullable = false)
    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @Column(nullable = false)
    private LocalDateTime registeredAt;

    @Column(length = 25, nullable = false)
    private String registeredBy;

    @Setter
    private LocalDateTime modifiedAt;

    @Setter
    @Column(length = 25)
    private String modifiedBy;

    protected Project() {}

    private Project(Member member, String title, String content) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.status = ProjectStatus.ENROLLING;
        this.registeredAt = LocalDateTime.now();
        this.registeredBy = member.getNickName();
        this.modifiedAt = LocalDateTime.now();
        this.modifiedBy = member.getNickName();
    }

    public static Project of(Member member, String title, String content) {
        return new Project(member, title, content);
    }
}
