package com.project.devtogether.project.dto;

import com.project.devtogether.skill.dto.SkillDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {
    private Long id;
    private String title;
    private String content;
    private List<SkillDto> skills;
    private String registeredBy;
}
