package com.project.devtogether.participant.domain;

import com.project.devtogether.member.domain.Member;
import com.project.devtogether.participant.domain.enums.ParticipantStatus;
import com.project.devtogether.project.domain.Project;
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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(of = "id")
@Entity
public class ProjectMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Setter
    @Column(length = 25, nullable = false)
    @Enumerated(EnumType.STRING)
    private ParticipantStatus status;

    @Column(length = 500)
    private String comment;

    protected ProjectMember() {}

    private ProjectMember(Project project, Member member) {
        setProject(project);
        setMember(member);
        this.status = ParticipantStatus.APPLIED;
    }

    public static ProjectMember of(Project project, Member member) {
        return new ProjectMember(project, member);
    }

    /*
     * 연관관계 메서드
     */
    public void setProject(Project project) {
        this.project = project;
        project.getMembers().add(this);
    }

    public void setMember(Member member) {
        this.member = member;
        member.getProjectMembers().add(this);
    }
}

