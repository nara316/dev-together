package com.project.devtogether.skill.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProjectSkill is a Querydsl query type for ProjectSkill
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProjectSkill extends EntityPathBase<ProjectSkill> {

    private static final long serialVersionUID = 1384065028L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProjectSkill projectSkill = new QProjectSkill("projectSkill");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.project.devtogether.project.domain.QProject project;

    public final QSkill skill;

    public QProjectSkill(String variable) {
        this(ProjectSkill.class, forVariable(variable), INITS);
    }

    public QProjectSkill(Path<? extends ProjectSkill> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProjectSkill(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProjectSkill(PathMetadata metadata, PathInits inits) {
        this(ProjectSkill.class, metadata, inits);
    }

    public QProjectSkill(Class<? extends ProjectSkill> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.project = inits.isInitialized("project") ? new com.project.devtogether.project.domain.QProject(forProperty("project"), inits.get("project")) : null;
        this.skill = inits.isInitialized("skill") ? new QSkill(forProperty("skill")) : null;
    }

}

