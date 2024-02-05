package com.project.devtogether.skill.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectSkillRepository extends JpaRepository<ProjectSkill, Long> {

    public void deleteAllByProjectId(Long projectId);
}
