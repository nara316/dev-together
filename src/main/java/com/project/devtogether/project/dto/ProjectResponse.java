package com.project.devtogether.project.dto;

import com.project.devtogether.project.domain.Project;
import com.project.devtogether.skill.dto.SkillDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;

@Builder
public record ProjectResponse(
        Long id,
        String title,
        String content,
        List<SkillDto> skills,
        String registeredBy
) {

    public static ProjectResponse of(Project project) {
        return ProjectResponse.builder()
                .id(project.getId())
                .title(project.getTitle())
                .content(project.getContent())
                .skills(project.getSkills().stream()
                        .map(it -> it.getSkill())
                        .map(it -> new SkillDto(it.getName())).collect(Collectors.toList()))
                .registeredBy(project.getRegisteredBy())
                .build();
    }
}
