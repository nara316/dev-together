package com.project.devtogether.member.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 1697212433L;

    public static final QMember member = new QMember("member1");

    public final DateTimePath<java.time.LocalDateTime> createAt = createDateTime("createAt", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath introduce = createString("introduce");

    public final DateTimePath<java.time.LocalDateTime> lastLoginAt = createDateTime("lastLoginAt", java.time.LocalDateTime.class);

    public final StringPath name = createString("name");

    public final StringPath nickName = createString("nickName");

    public final StringPath password = createString("password");

    public final ListPath<com.project.devtogether.participant.domain.ProjectMember, com.project.devtogether.participant.domain.QProjectMember> projectMembers = this.<com.project.devtogether.participant.domain.ProjectMember, com.project.devtogether.participant.domain.QProjectMember>createList("projectMembers", com.project.devtogether.participant.domain.ProjectMember.class, com.project.devtogether.participant.domain.QProjectMember.class, PathInits.DIRECT2);

    public final ListPath<com.project.devtogether.project.domain.Project, com.project.devtogether.project.domain.QProject> projects = this.<com.project.devtogether.project.domain.Project, com.project.devtogether.project.domain.QProject>createList("projects", com.project.devtogether.project.domain.Project.class, com.project.devtogether.project.domain.QProject.class, PathInits.DIRECT2);

    public final EnumPath<com.project.devtogether.member.domain.enums.MemberRole> role = createEnum("role", com.project.devtogether.member.domain.enums.MemberRole.class);

    public final EnumPath<com.project.devtogether.member.domain.enums.MemberStatus> status = createEnum("status", com.project.devtogether.member.domain.enums.MemberStatus.class);

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

