package com.project.devtogether.project.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProject is a Querydsl query type for Project
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProject extends EntityPathBase<Project> {

    private static final long serialVersionUID = 2071307333L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProject project = new QProject("project");

    public final DateTimePath<java.time.LocalDateTime> advertiseEndDate = createDateTime("advertiseEndDate", java.time.LocalDateTime.class);

    public final StringPath content = createString("content");

    public final NumberPath<Integer> currentCapacity = createNumber("currentCapacity", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.project.devtogether.member.domain.QMember member;

    public final ListPath<com.project.devtogether.participant.domain.ProjectMember, com.project.devtogether.participant.domain.QProjectMember> members = this.<com.project.devtogether.participant.domain.ProjectMember, com.project.devtogether.participant.domain.QProjectMember>createList("members", com.project.devtogether.participant.domain.ProjectMember.class, com.project.devtogether.participant.domain.QProjectMember.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> modifiedAt = createDateTime("modifiedAt", java.time.LocalDateTime.class);

    public final StringPath modifiedBy = createString("modifiedBy");

    public final NumberPath<Integer> recruitCapacity = createNumber("recruitCapacity", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> registeredAt = createDateTime("registeredAt", java.time.LocalDateTime.class);

    public final StringPath registeredBy = createString("registeredBy");

    public final ListPath<com.project.devtogether.skill.domain.ProjectSkill, com.project.devtogether.skill.domain.QProjectSkill> skills = this.<com.project.devtogether.skill.domain.ProjectSkill, com.project.devtogether.skill.domain.QProjectSkill>createList("skills", com.project.devtogether.skill.domain.ProjectSkill.class, com.project.devtogether.skill.domain.QProjectSkill.class, PathInits.DIRECT2);

    public final EnumPath<com.project.devtogether.project.domain.enums.ProjectStatus> status = createEnum("status", com.project.devtogether.project.domain.enums.ProjectStatus.class);

    public final StringPath title = createString("title");

    public QProject(String variable) {
        this(Project.class, forVariable(variable), INITS);
    }

    public QProject(Path<? extends Project> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProject(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProject(PathMetadata metadata, PathInits inits) {
        this(Project.class, metadata, inits);
    }

    public QProject(Class<? extends Project> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.project.devtogether.member.domain.QMember(forProperty("member")) : null;
    }

}

