package com.project.devtogether.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/*
 * project 관련 2000번대 에러코드 사용
 * projectSkill 관련 3000번대 에러코드 사용
 * projectMember 관련 4000번대 에러코드 사용
 * */
@AllArgsConstructor
@Getter
public enum ProjectErrorCode implements ErrorCodeIfs{

    PROJECT_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), 2401, "해당 게시글을 찾을 수 없습니다"),
    PROJECT_NOT_QUALIFIED(HttpStatus.BAD_REQUEST.value(), 2402, "해당 권한이 없습니다"),
    PROJECT_END_DATE_CANNOT_BEFORE_REGISTER_AT(
            HttpStatus.BAD_REQUEST.value(), 2403, "현재 날짜보다 과거를 입력할 수 없습니다"),
    PROJECT_STATUS_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), 2404, "해당 상태값을 찾을 수 없습니다"),
    SKILL_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), 3401, "해당 스킬을 찾을 수 없습니다"),
    PROJECT_MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), 4401, "해당 신청을 찾을 수 없습니다"),
    PROJECT_MEMBER_NOT_QUALIFIED(HttpStatus.BAD_REQUEST.value(), 4402, "해당 권한이 없습니다"),
    PROJECT_MEMBER_CANNOT_APPLY_NOT_ENROLLING(
            HttpStatus.BAD_REQUEST.value(), 4403, "모집중 외의 게시글은 신청할 수 없습니다"),
    PROJECT_MEMBER_CANNOT_UPDATE_NOT_APPLIED(
            HttpStatus.BAD_REQUEST.value(), 4402, "신청단계 외는 변경할 수 없습니다."),
    ;

    private final Integer HttpStatusCode;
    private final Integer errorCode;
    private final String description;
}
