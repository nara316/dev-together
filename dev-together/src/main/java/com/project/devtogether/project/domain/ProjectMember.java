package com.project.devtogether.project.domain;

import com.project.devtogether.member.domain.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
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

    protected ProjectMember() {}

    private ProjectMember(Project project, Member member) {
        setProject(project);
        this.member = member;
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
}
