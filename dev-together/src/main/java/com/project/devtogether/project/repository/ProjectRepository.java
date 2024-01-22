package com.project.devtogether.project.repository;

import com.project.devtogether.project.domain.Project;
import com.project.devtogether.project.domain.enums.ProjectStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long>, ProjectCustomRepository {

    List<Project> findAllByStatus(ProjectStatus status);
}
