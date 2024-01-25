package com.project.devtogether.project.service;

import com.project.devtogether.common.error.ErrorCode;
import com.project.devtogether.common.exception.ApiException;
import com.project.devtogether.common.security.util.SecurityUtil;
import com.project.devtogether.member.domain.Member;
import com.project.devtogether.member.domain.MemberRepository;
import com.project.devtogether.participant.domain.ProjectMember;
import com.project.devtogether.participant.domain.ProjectMemberRepository;
import com.project.devtogether.participant.domain.enums.ParticipantStatus;
import com.project.devtogether.participant.domain.enums.ParticipantUpdateStatus;
import com.project.devtogether.participant.dto.ProjectMemberResponse;
import com.project.devtogether.participant.dto.ProjectMemberUpdateRequest;
import com.project.devtogether.project.domain.Project;
import com.project.devtogether.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectMemberService {

    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;

    private final ProjectMemberRepository projectMemberRepository;

    public ProjectMemberResponse apply(Long id) {
        Member member = getReferenceBySecurity();
        Project project = getProjectById(id);

        ProjectMember projectMember = ProjectMember.of(project, member);
        projectMemberRepository.save(projectMember);
        return ProjectMemberResponse.of(projectMember);
    }

    public ProjectMemberResponse cancel(Long id) {
        Member member = getReferenceBySecurity();
        ProjectMember projectMember = getProjectMemberById(id);
        checkQualifiedBySecurity(member.getId(), projectMember.getMember().getId());

        //TODO:: 신청 단계가 아닌 다른 단계에서도 취소할 수 있게 해야함 (하지만 기준이 필요)
        if (projectMember.getStatus() != ParticipantStatus.APPLIED) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "신청 단계가 아닌 경우 취소할 수 없습니다.");
        }
        projectMember.setStatus(ParticipantStatus.CANCELED);
        return ProjectMemberResponse.of(projectMember);
    }

    public ProjectMemberResponse checkApply(Long id, ParticipantUpdateStatus participantUpdateStatus,
                                                ProjectMemberUpdateRequest projectMemberUpdateRequest) {

        Member member = getReferenceBySecurity();
        ProjectMember projectMember = getProjectMemberById(id);
        checkQualifiedBySecurity(member.getId(), projectMember.getProject().getMember().getId());

        projectMember.setComment(projectMemberUpdateRequest.comment());
        switch (participantUpdateStatus) {
            case ACCEPTED -> projectMember.setStatus(ParticipantStatus.ACCEPTED);
            case REJECTED -> projectMember.setStatus(ParticipantStatus.REJECTED);
        };
        return ProjectMemberResponse.of(projectMember);
    }

    private Member getReferenceBySecurity() {
        return memberRepository.getReferenceById(SecurityUtil.getCurrentUserId());
    }

    private Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.BAD_REQUEST, "해당 게시글을 찾을 수 없습니다."));
    }

    private void checkQualifiedBySecurity(Long securityId, Long projectMemberId) {
        if (securityId != projectMemberId) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "해당 권한이 없습니다.");
        }
    }

    private ProjectMember getProjectMemberById(Long id) {
        ProjectMember projectMember = projectMemberRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.BAD_REQUEST, "해당 신청이 존재하지 않습니다."));
        return projectMember;
    }
}
