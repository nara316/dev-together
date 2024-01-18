package com.project.devtogether.project.repository;

import com.project.devtogether.project.dto.ProjectDto;
import com.project.devtogether.project.dto.ProjectResponse;
import java.util.List;

public interface ProjectCustomRepository {

    List<ProjectDto> findProject(Long id);
}
