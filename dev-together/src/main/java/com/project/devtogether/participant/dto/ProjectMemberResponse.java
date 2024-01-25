package com.project.devtogether.participant.dto;

import com.project.devtogether.participant.domain.ProjectMember;
import lombok.Builder;

@Builder
public record ProjectMemberResponse(
        String status,
        String comment
) {
    public static ProjectMemberResponse of(ProjectMember projectMember) {
        return ProjectMemberResponse.builder()
                .status(projectMember.getStatus().name())
                .comment(projectMember.getComment())
                .build();
    }
}
