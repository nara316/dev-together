package com.project.devtogether.project.service;

import static com.project.devtogether.common.redis.enums.RedisCache.PROJECT_REDIS_KEY;
import static com.project.devtogether.common.redis.enums.RedisCache.REDIS_DURATION;

import com.project.devtogether.common.error.ProjectErrorCode;
import com.project.devtogether.common.exception.ApiException;
import com.project.devtogether.common.redis.config.ObjectSerializer;
import com.project.devtogether.common.redis.service.RedisService;
import com.project.devtogether.common.security.util.SecurityUtil;
import com.project.devtogether.member.domain.Member;
import com.project.devtogether.member.domain.MemberRepository;
import com.project.devtogether.participant.domain.ProjectMember;
import com.project.devtogether.participant.domain.ProjectMemberRepository;
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
import java.util.Optional;
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
    private final ProjectMemberRepository projectMemberRepository;
    private final ObjectSerializer objectSerializer;
    private final RedisService redisService;

    public ProjectResponse register(ProjectRegisterRequest request) {
        Member member = getReferenceBySecurity();
        LocalDateTime endDate = calculateAdvertiseEndDate(request.advertiseEndDate());
        Project project = Project.of(member, request.title(), request.content(), request.recruitCapacity(), endDate);
        project.addCurrentCapacity();
        projectRepository.save(project);

        List<String> skills = request.skills();
        for (String skill : skills) {
            Skill findSkill = skillRepository.findById(Long.parseLong(skill))
                    .orElseThrow(() -> new ApiException(ProjectErrorCode.SKILL_NOT_FOUND));
            ProjectSkill projectSkill = ProjectSkill.of(project, findSkill);
            projectSkillRepository.save(projectSkill);
        }

        ProjectMember projectMember = ProjectMember.of(project, member);
        projectMemberRepository.save(projectMember);
        return ProjectResponse.of(project);
    }

    @Transactional(readOnly = true)
    public List<ProjectDto> readProject(Long id) {
        String redisKey = PROJECT_REDIS_KEY.getValue() + id;
        Optional<List<ProjectDto>> cache = objectSerializer.getDataList(redisKey, List.class, ProjectDto.class);
        if (cache.isPresent()) {
            return cache.get();
        }

        List<ProjectDto> response = projectRepository.findProject(id);
        objectSerializer.saveData(redisKey, response, Integer.parseInt(REDIS_DURATION.getValue()));
        return response;
    }

    @Transactional(readOnly = true)
    public Page<ProjectDto> readProjects(SearchType searchType, String searchValue, Pageable pageable
    ) {
        if (searchValue == null || searchValue.isBlank()) {
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

    //redis 삭제 후 다시 저장
    public List<ProjectDto> updateProject(Long id, ProjectUpdateRequest request) {
        String redisKey = PROJECT_REDIS_KEY.getValue() + id;
        Optional<List<ProjectDto>> cache = objectSerializer.getDataList(redisKey, List.class, ProjectDto.class);
        if (cache.isPresent()) {
            redisService.deleteValues(redisKey);
        }

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
                    .orElseThrow(() -> new ApiException(ProjectErrorCode.SKILL_NOT_FOUND));
            ProjectSkill projectSkill = ProjectSkill.of(project, findSkill);
            projectSkillRepository.save(projectSkill);
        }

        List<ProjectDto> response = List.of(ProjectDto.of(project));
        objectSerializer.saveData(redisKey, response, Integer.parseInt(REDIS_DURATION.getValue()));
        return response;
    }

    public List<ProjectDto> updateProjectAdEndDate(Long id, Long plusDate) {
        String redisKey = PROJECT_REDIS_KEY.getValue() + id;
        Optional<List<ProjectDto>> cache = objectSerializer.getDataList(redisKey, List.class, ProjectDto.class);
        if (cache.isPresent()) {
            redisService.deleteValues(redisKey);
        }
        Member member = getReferenceBySecurity();
        Project project = getProjectById(id);
        checkQualifiedBySecurity(member.getId(), project.getMember().getId());

        LocalDateTime previousEndDate = project.getAdvertiseEndDate();
        project.setAdvertiseEndDate(previousEndDate.plusDays(plusDate));

        List<ProjectDto> response = List.of(ProjectDto.of(project));
        objectSerializer.saveData(redisKey, response, Integer.parseInt(REDIS_DURATION.getValue()));
        return response;
    }

    public void deleteProject(Long id) {
        String redisKey = PROJECT_REDIS_KEY.getValue() + id;
        Optional<List<ProjectDto>> cache = objectSerializer.getDataList(redisKey, List.class, ProjectDto.class);
        if (cache.isPresent()) {
            redisService.deleteValues(redisKey);
        }
        Member member = getReferenceBySecurity();
        Project project = getProjectById(id);
        checkQualifiedBySecurity(member.getId(), project.getMember().getId());

        //ProjectSkill, ProjectMember도 삭제
        projectSkillRepository.deleteAllByProjectId(project.getId());
        projectMemberRepository.deleteAllByProjectId(project.getId());
        projectRepository.deleteById(project.getId());

    }

    private Member getReferenceBySecurity() {
        return memberRepository.getReferenceById(SecurityUtil.getCurrentUserId());
    }

    private Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ApiException(ProjectErrorCode.PROJECT_NOT_FOUND));
    }

    private void checkQualifiedBySecurity(Long securityId, Long projectId) {
        if (securityId != projectId) {
            throw new ApiException(ProjectErrorCode.PROJECT_NOT_QUALIFIED);
        }
    }

    private LocalDateTime calculateAdvertiseEndDate(LocalDateTime advertiseEndDate) {
        if (advertiseEndDate == null) {
            return LocalDateTime.now().plusDays(7);
        }
        if (advertiseEndDate.isBefore(LocalDateTime.now())) {
            throw new ApiException(ProjectErrorCode.PROJECT_END_DATE_CANNOT_BEFORE_REGISTER_AT);
        }
        return advertiseEndDate;
    }
}
