package com.project.devtogether.project.repository;

import com.project.devtogether.project.dto.ProjectDto;
import com.project.devtogether.project.dto.ProjectResponse;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectCustomRepository {

    List<ProjectDto> findProject(Long id);
    Page<ProjectDto> findProjects(Pageable pageable);

    Page<ProjectDto> findProjectsByTitle(String searchType, Pageable pageable);

    Page<ProjectDto> findProjectsByNickName(String searchType, Pageable pageable);

    Page<ProjectDto> findProjectsBySkills(Collection<String> skills, Pageable pageable);
}
