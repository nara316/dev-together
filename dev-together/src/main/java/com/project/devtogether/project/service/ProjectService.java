package com.project.devtogether.project.service;

import com.project.devtogether.common.error.ErrorCode;
import com.project.devtogether.common.exception.ApiException;
import com.project.devtogether.common.security.util.SecurityUtil;
import com.project.devtogether.member.domain.Member;
import com.project.devtogether.member.domain.MemberRepository;
import com.project.devtogether.project.domain.Project;
import com.project.devtogether.project.dto.ProjectDto;
import com.project.devtogether.project.domain.enums.SearchType;
import com.project.devtogether.project.repository.ProjectRepository;
import com.project.devtogether.project.domain.enums.ProjectStatus;
import com.project.devtogether.project.dto.ProjectRegisterRequest;
import com.project.devtogether.project.dto.ProjectResponse;
import com.project.devtogether.project.dto.ProjectUpdateRequest;
import com.project.devtogether.skill.domain.ProjectSkill;
import com.project.devtogether.skill.domain.ProjectSkillRepository;
import com.project.devtogether.skill.domain.Skill;
import com.project.devtogether.skill.domain.SkillRepository;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final SkillRepository skillRepository;
    private final ProjectSkillRepository projectSkillRepository;

    public ProjectResponse register(ProjectRegisterRequest request) {
        Member member = getReferenceBySecurity();
        LocalDateTime endDate = calculateAdvertiseEndDate(request.advertisementEndDate());
        Project project = Project.of(member, request.title(), request.content(), endDate);
        projectRepository.save(project);

        List<String> skills = request.skills();
        for (String skill : skills) {
            Skill findSkill = skillRepository.findById(Long.parseLong(skill))
                    .orElseThrow(() -> new ApiException(ErrorCode.BAD_REQUEST, "해당 스킬을 찾을 수 없습니다."));
            ProjectSkill projectSkill = ProjectSkill.of(project, findSkill);
            projectSkillRepository.save(projectSkill);
        }
        return ProjectResponse.of(project);
    }

    @Transactional(readOnly = true)
    public List<ProjectDto> readProject(Long id) {
        return projectRepository.findProject(id);
    }

    @Transactional(readOnly = true)
    public Page<ProjectDto> readProjects(SearchType searchType, String searchValue, Pageable pageable
    ) {
        if (searchValue.isBlank() || searchValue == null) {
            return projectRepository.findProjects(pageable);
        }

        return switch (searchType) {
            case TITLE -> projectRepository.findProjectsByTitle(searchValue, pageable);
            case NICKNAME -> projectRepository.findProjectsByNickName(searchValue, pageable);
            case SKILL -> projectRepository.findProjectsBySkills(
                            Arrays.stream(searchValue.split(" ")).toList(),
                            pageable
                    );
        };
    }

    public ProjectResponse updateProject(Long id, ProjectUpdateRequest request) {
        Member member = getReferenceBySecurity();
        Project project = getProjectById(id);
        checkQualifiedBySecurity(member.getId(), project.getMember().getId());

        project.setContent(request.content());
        project.setStatus(ProjectStatus.getStatusByInput(request.status()));
        project.setModifiedAt(LocalDateTime.now());
        project.setModifiedBy(member.getNickName());

        //projectSkill (삭제 -> 다시 SAVE?)
        projectSkillRepository.deleteAllByProjectId(project.getId());
        List<String> updateSkills = request.skills();
        for (String skill : updateSkills) {
            Skill findSkill = skillRepository.findById(Long.parseLong(skill))
                    .orElseThrow(() -> new ApiException(ErrorCode.BAD_REQUEST, "해당 스킬을 찾을 수 없습니다."));
            ProjectSkill projectSkill = ProjectSkill.of(project, findSkill);
            projectSkillRepository.save(projectSkill);
        }

        return ProjectResponse.of(project);
    }

    public ProjectResponse updateProjectAdEndDate(Long id, Long plusDate) {
        Member member = getReferenceBySecurity();
        Project project = getProjectById(id);
        checkQualifiedBySecurity(member.getId(), project.getMember().getId());

        LocalDateTime previousEndDate = project.getAdvertiseEndDate();
        project.setAdvertiseEndDate(previousEndDate.plusDays(plusDate));
        return ProjectResponse.of(project);
    }

    public void deleteProject(Long id) {
        Member member = getReferenceBySecurity();
        Project project = getProjectById(id);
        checkQualifiedBySecurity(member.getId(), project.getMember().getId());

        //ProjectSkill도 삭제
        projectSkillRepository.deleteAllByProjectId(project.getId());
        projectRepository.deleteById(project.getId());
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

    private LocalDateTime calculateAdvertiseEndDate(LocalDateTime advertisementEndDate) {
        if (advertisementEndDate.isBefore(LocalDateTime.now())) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "현재 날짜보다 과거를 입력할 수 없습니다.");
        }
        if (advertisementEndDate == null) {
            return LocalDateTime.now().plusDays(7);
        }
        return advertisementEndDate;
    }
}
