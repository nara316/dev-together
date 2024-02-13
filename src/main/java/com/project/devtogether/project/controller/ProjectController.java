package com.project.devtogether.project.controller;

import com.project.devtogether.common.api.Api;
import com.project.devtogether.project.dto.ProjectDto;
import com.project.devtogether.project.dto.ProjectRegisterRequest;
import com.project.devtogether.project.dto.ProjectResponse;
import com.project.devtogether.project.dto.ProjectUpdateRequest;
import com.project.devtogether.project.domain.enums.SearchType;
import com.project.devtogether.project.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @Operation(summary = "게시글 작성 API")
    @PostMapping("/register")
    public Api<ProjectResponse> register(@Valid @RequestBody ProjectRegisterRequest request) {
        ProjectResponse result = projectService.register(request);
        return Api.OK(result);
    }

    /*
    * intelliJ로 빌드하니 부트 3.2 부터는 바이트코드를 파싱하지 않는다. -parameters 옵션을 생략하지 않기로 결정
    * */
    @Operation(summary = "게시글 조회 API", description = "해당 게시글ID의 게시글 정보를 조회한다.")
    @GetMapping("{id}")
    public Api<List<ProjectDto>> readProject(@PathVariable("id") Long id) {
        List<ProjectDto> result = projectService.readProject(id);
        return Api.OK(result);
    }

    @Operation(summary = "게시글 조회 API", description = "검색타입과 검색어를 기반으로 최신작성 순으로 게시글들을 조회한다.")
    @GetMapping
    public Api<Page<ProjectDto>> readProjects(
            @RequestParam(required = false, name = "searchType") SearchType searchType, //검색타입
            @RequestParam(required = false, name = "searchValue") String searchValue, //검색어
            Pageable pageable
    ) {
        Page<ProjectDto> result = projectService.readProjects(searchType, searchValue, pageable);
        return Api.OK(result);
    }

    @Operation(summary = "게시글 정보 수정 API")
    @PatchMapping("{id}")
    public Api<ProjectResponse> updateProject(@PathVariable("id") Long id,
                                              @Valid @RequestBody ProjectUpdateRequest request) {
        ProjectResponse result = projectService.updateProject(id, request);
        return Api.OK(result);
    }

    @Operation(summary = "게시글 기한 수정 API", description = "게시글 공고 기한을 늘린다.")
    @PatchMapping("{id}/{plusDate}")
    public Api<ProjectResponse> updateProjectAdEndDate(
            @PathVariable("id") Long id, @PathVariable("plusDate") Long plusDate) {
        ProjectResponse result = projectService.updateProjectAdEndDate(id, plusDate);
        return Api.OK(result);
    }

    @Operation(summary = "게시글 삭제 API")
    @DeleteMapping("{id}")
    public Api<String> deleteProject(@PathVariable("id") Long id) {
        projectService.deleteProject(id);
        return Api.OK("삭제가 완료되었습니다.");
    }
}
