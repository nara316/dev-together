package com.project.devtogether.skill.domain;

import com.project.devtogether.project.domain.Project;
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
@EqualsAndHashCode
@Entity
public class ProjectSkill {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id")
    private Skill skill;

    protected ProjectSkill() {}

    private ProjectSkill(Project project, Skill skill) {
        setProject(project);
        this.skill = skill;
    }

    public static ProjectSkill of(Project project, Skill skill) {
        return new ProjectSkill(project, skill);
    }

    /*
     * 연관관계 메서드
     */
    public void setProject(Project project) {
        this.project = project;
        project.getProjectSkills().add(this);
    }
}
