package com.project.devtogether.project.repository;

import com.project.devtogether.project.domain.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
}
