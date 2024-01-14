package com.project.devtogether.project.service;

import com.project.devtogether.common.error.ErrorCode;
import com.project.devtogether.common.exception.ApiException;
import com.project.devtogether.common.security.util.SecurityUtil;
import com.project.devtogether.member.domain.Member;
import com.project.devtogether.member.domain.MemberRepository;
import com.project.devtogether.project.domain.Project;
import com.project.devtogether.project.domain.ProjectRepository;
import com.project.devtogether.project.domain.enums.ProjectStatus;
import com.project.devtogether.project.dto.ProjectRegisterRequest;
import com.project.devtogether.project.dto.ProjectResponse;
import com.project.devtogether.project.dto.ProjectUpdateRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;

    public ProjectResponse register(ProjectRegisterRequest request) {
        Member member = getReferenceBySecurity();
        Project project = Project.of(member, request.title(), request.content());
        projectRepository.save(project);
        return ProjectResponse.of(project);
    }

    @Transactional(readOnly = true)
    public ProjectResponse readProject(Long id) {
        Project project = getProjectById(id);
        return ProjectResponse.of(project);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> readEnrollingProjects() {
        List<Project> projects = projectRepository.findAllByStatus(ProjectStatus.ENROLLING);
        return projects.stream()
                .map(ProjectResponse::of)
                .collect(Collectors.toList());
    }

    public ProjectResponse updateProject(Long id, ProjectUpdateRequest request) {
        Member member = getReferenceBySecurity();
        Project project = getProjectById(id);
        checkQualifiedBySecurity(member.getId(), project.getMember().getId());

        project.setContent(request.content());
        project.setStatus(ProjectStatus.getStatusByInput(request.status()));
        project.setModifiedAt(LocalDateTime.now());
        project.setModifiedBy(member.getNickName());
        return ProjectResponse.of(project);
    }

    public void deleteProject(Long id) {
        Member member = getReferenceBySecurity();
        Project project = getProjectById(id);
        checkQualifiedBySecurity(member.getId(), project.getMember().getId());

        memberRepository.deleteById(project.getId());
    }

    private Member getReferenceBySecurity() {
        return memberRepository.getReferenceById(SecurityUtil.getCurrentUserId());
    }

    private Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.BAD_REQUEST, "해당 게시글을 찾을 수 없습니다."));
    }

    private void checkQualifiedBySecurity(Long securityId, Long projectId) {
        if (securityId != projectId) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "해당 권한이 없습니다.");
        }
    }
}
