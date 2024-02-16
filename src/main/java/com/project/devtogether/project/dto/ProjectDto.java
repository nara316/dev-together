package com.project.devtogether.project.dto;

import com.project.devtogether.project.domain.Project;
import com.project.devtogether.skill.dto.SkillDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {
    private Long id;
    private String title;
    private String content;
    private List<SkillDto> skills;
    private String registeredBy;

    public static ProjectDto of(Project project) {
        return ProjectDto.builder()
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
