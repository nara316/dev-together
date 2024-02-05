package com.project.devtogether.participant.domain.enums;

import com.project.devtogether.common.error.ProjectErrorCode;
import com.project.devtogether.common.exception.ApiException;

public enum ParticipantStatus {
    APPLIED, //신청
    CANCELED, //취소
    ACCEPTED, //승인
    REJECTED, //거부
    ;

    public static void checkStatusIsApplied(ParticipantStatus participantStatus) {
        if (participantStatus != APPLIED) {
            throw new ApiException(ProjectErrorCode.PROJECT_MEMBER_CANNOT_UPDATE_NOT_APPLIED);
        }
    }
}
