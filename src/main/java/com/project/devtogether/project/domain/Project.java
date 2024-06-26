package com.project.devtogether.project.domain;

import com.project.devtogether.member.domain.Member;
import com.project.devtogether.participant.domain.ProjectMember;
import com.project.devtogether.project.domain.enums.ProjectStatus;
import com.project.devtogether.skill.domain.ProjectSkill;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(of = "id")
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
    private Integer recruitCapacity;

    @Setter
    private Integer currentCapacity;

    @Version
    private int version;

    @Setter
    private LocalDateTime advertiseEndDate;

    @OneToMany(mappedBy = "project")
    private List<ProjectSkill> skills = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    private List<ProjectMember> members = new ArrayList<>();

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

    private Project(Member member, String title, String content, Integer recruitCapacity, LocalDateTime advertisementEndDate) {
        setMember(member);
        this.title = title;
        this.content = content;
        this.status = ProjectStatus.ENROLLING;
        this.recruitCapacity = recruitCapacity;
        this.currentCapacity = 0;
        this.advertiseEndDate = advertisementEndDate;
        this.registeredAt = LocalDateTime.now();
        this.registeredBy = member.getNickName();
        this.modifiedAt = LocalDateTime.now();
        this.modifiedBy = member.getNickName();
    }

    public static Project of(Member member, String title, String content, Integer recruitCapacity, LocalDateTime advertisementEndDate) {
        return new Project(member, title, content, recruitCapacity, advertisementEndDate);
    }

    public void addCurrentCapacity() {
        this.currentCapacity++;
        if (currentCapacity == recruitCapacity) {
            status = ProjectStatus.CLOSED;
        }
    }

    public void subtractCurrentCapacity() {
        this.currentCapacity--;
        if (currentCapacity < recruitCapacity) {
            status = ProjectStatus.ENROLLING;
        }
    }

    /*
     * 연관관계 메서드
     */
    public void setMember(Member member) {
        this.member = member;
        member.getProjects().add(this);
    }
}
