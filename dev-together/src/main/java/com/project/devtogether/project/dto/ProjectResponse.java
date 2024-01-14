package com.project.devtogether.project.dto;

import com.project.devtogether.project.domain.Project;

public record ProjectResponse(
        Long id,
        Long memberId,
        String title,
        String content,
        String status,
        String registeredBy
) {
    public static ProjectResponse of(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getMember().getId(),
                project.getTitle(),
                project.getContent(),
                project.getStatus().getStatus(),
                project.getRegisteredBy()
        );
    }
}
