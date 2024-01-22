package com.project.devtogether.project.controller;

import com.project.devtogether.common.api.Api;
import com.project.devtogether.project.dto.ProjectDto;
import com.project.devtogether.project.dto.ProjectRegisterRequest;
import com.project.devtogether.project.dto.ProjectResponse;
import com.project.devtogether.project.dto.ProjectUpdateRequest;
import com.project.devtogether.project.domain.enums.SearchType;
import com.project.devtogether.project.service.ProjectService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    /*
    * intelliJ로 빌드하니 부트 3.2 부터는 바이트코드를 파싱하지 않는다. -parameters 옵션을 생략하지 않기로 결정
    * */
    @GetMapping("{id}")
    public Api<List<ProjectDto>> readProject(@PathVariable("id") Long id) {
        List<ProjectDto> result = projectService.readProject(id);
        return Api.OK(result);
    }

    @GetMapping
    public Api<Page<ProjectDto>> readProjects(
            @RequestParam(required = false, name = "searchType") SearchType searchType, //검색타입
            @RequestParam(required = false, name = "searchValue") String searchValue, //검색어
            @PageableDefault(size = 10, sort = "registeredBy", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        System.out.println(searchValue+"왜 안되징");
        Page<ProjectDto> result = projectService.readProjects(searchType, searchValue, pageable);
        return Api.OK(result);
    }

    @PatchMapping("{id}")
    public Api<ProjectResponse> updateProject(@PathVariable("id") Long id, ProjectUpdateRequest request) {
        ProjectResponse result = projectService.updateProject(id, request);
        return Api.OK(result);
    }

    @DeleteMapping("{id}")
    public Api<String> deleteProject(@PathVariable("id") Long id) {
        projectService.deleteProject(id);
        return Api.OK("삭제가 완료되었습니다.");
    }
}
