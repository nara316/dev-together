package com.project.devtogether.project.repository;

import static com.project.devtogether.project.domain.QProject.project;
import static com.project.devtogether.skill.domain.QProjectSkill.projectSkill;
import static com.project.devtogether.skill.domain.QSkill.skill;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import com.project.devtogether.project.domain.enums.ProjectStatus;
import com.project.devtogether.project.dto.ProjectDto;
import com.project.devtogether.project.dto.ProjectResponse;
import com.project.devtogether.skill.dto.SkillDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    @Override
    public Page<ProjectDto> findProjects(Pageable pageable) {
        List<ProjectDto> projects = queryFactory
                .selectFrom(projectSkill)
                .leftJoin(projectSkill.project, project)
                .leftJoin(projectSkill.skill, skill)
                .where(project.status.eq(ProjectStatus.ENROLLING))
                .orderBy(project.registeredAt.desc()) //최신순이 맨 위에 오게
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .transform(groupBy(project.id).list(
                        Projections.fields(ProjectDto.class,
                                project.id,
                                project.title,
                                project.content,
                                list(Projections.fields(SkillDto.class, skill.name)).as("skills"),
                                project.registeredBy)));

        JPAQuery<Long> countQuery = queryFactory
                .select(project.count())
                .from(project)
                .where(project.status.eq(ProjectStatus.ENROLLING));

        return new PageImpl<>(projects, pageable, countQuery.fetchOne());
    }
}
