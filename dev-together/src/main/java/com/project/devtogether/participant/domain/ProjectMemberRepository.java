package com.project.devtogether.participant.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {

    public void deleteAllByProjectId(Long projectId);
}
