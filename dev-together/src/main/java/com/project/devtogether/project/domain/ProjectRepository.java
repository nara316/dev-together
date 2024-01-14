package com.project.devtogether.project.domain;

import com.project.devtogether.project.domain.enums.ProjectStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    public List<Project> findAllByStatus(ProjectStatus status);
}
