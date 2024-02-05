package com.project.devtogether.project.controller;

import com.project.devtogether.common.api.Api;
import com.project.devtogether.participant.domain.enums.ParticipantUpdateStatus;
import com.project.devtogether.participant.dto.ProjectMemberResponse;
import com.project.devtogether.participant.dto.ProjectMemberUpdateRequest;
import com.project.devtogether.project.service.ProjectMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/project/participation")
@RequiredArgsConstructor
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;

    @GetMapping("{projectId}")
    public Api<ProjectMemberResponse> apply(@PathVariable("projectId") Long projectId) {
        ProjectMemberResponse result = projectMemberService.apply(projectId);
        return Api.OK(result);
    }

    @PatchMapping("{projectMemberId}")
    public Api<ProjectMemberResponse> cancel(@PathVariable("projectMemberId") Long projectMemberId) {
        ProjectMemberResponse result = projectMemberService.cancel(projectMemberId);
        return Api.OK(result);
    }

    @PatchMapping("check/{projectMemberId}")
    public Api<ProjectMemberResponse> checkApply(
            @PathVariable("projectMemberId") Long projectMemberId,
            @RequestParam(name = "participantUpdateStatus") ParticipantUpdateStatus participantUpdateStatus,
            @Valid @RequestBody ProjectMemberUpdateRequest projectMemberUpdateRequest) {
        ProjectMemberResponse result = projectMemberService.checkApply(
                projectMemberId, participantUpdateStatus, projectMemberUpdateRequest);
        return Api.OK(result);
    }
}
