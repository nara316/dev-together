package com.project.devtogether.project.controller;

import com.project.devtogether.common.api.Api;
import com.project.devtogether.member.dto.MemberResponse;
import com.project.devtogether.project.dto.ProjectRegisterRequest;
import com.project.devtogether.project.dto.ProjectResponse;
import com.project.devtogether.project.dto.ProjectUpdateRequest;
import com.project.devtogether.project.service.ProjectService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/register")
    public Api<ProjectResponse> register(ProjectRegisterRequest request) {
        ProjectResponse result = projectService.register(request);
        return Api.OK(result);
    }

    @GetMapping("/{id}")
    public Api<ProjectResponse> readProject(@PathVariable Long id) {
        ProjectResponse result = projectService.readProject(id);
        return Api.OK(result);
    }

    @GetMapping
    public Api<List<ProjectResponse>> readEnrollingProjects() {
        List<ProjectResponse> result = projectService.readEnrollingProjects();
        return Api.OK(result);
    }

    @PatchMapping("{id}")
    public Api<ProjectResponse> updateProject(@PathVariable Long id, ProjectUpdateRequest request) {
        ProjectResponse result = projectService.updateProject(id, request);
        return Api.OK(result);
    }
}
