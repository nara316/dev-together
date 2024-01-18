package com.project.devtogether.project.repository;

import static com.project.devtogether.project.domain.QProject.project;
import static com.project.devtogether.skill.domain.QProjectSkill.projectSkill;
import static com.project.devtogether.skill.domain.QSkill.skill;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import com.project.devtogether.project.dto.ProjectDto;
import com.project.devtogether.project.dto.ProjectResponse;
import com.project.devtogether.skill.dto.SkillDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProjectCustomRepositoryImpl implements ProjectCustomRepository{

    private final JPAQueryFactory queryFactory;


    @Override
    public List<ProjectDto> findProject(Long id) {

        return queryFactory
                .selectFrom(projectSkill)
                .leftJoin(projectSkill.project, project)
                .leftJoin(projectSkill.skill, skill)
                .where(project.id.eq(id))
                .transform(groupBy(project.id).list(
                        Projections.fields(ProjectDto.class,
                                project.id,
                                project.title,
                                project.content,
                                list(Projections.fields(SkillDto.class, skill.name)).as("skills"),
                                project.registeredBy)));
    }
}
