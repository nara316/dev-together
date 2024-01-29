package com.project.devtogether.project.domain.enums;

import com.project.devtogether.common.error.ErrorCode;
import com.project.devtogether.common.error.ProjectErrorCode;
import com.project.devtogether.common.exception.ApiException;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProjectStatus {
    ENROLLING("모집중"),
    CLOSED("모집완료");

    private final String status;

    public static ProjectStatus getStatusByInput(String status) {
        return Arrays.stream(values())
                .filter(it -> status.equals(it.status))
                .findFirst()
                .orElseThrow(() -> new ApiException(ProjectErrorCode.PROJECT_STATUS_NOT_FOUND));
    }

    public static void checkStatusIsEnrolling(ProjectStatus projectStatus) {
        if (projectStatus != CLOSED) {
            throw new ApiException(ProjectErrorCode.PROJECT_MEMBER_CANNOT_APPLY_NOT_ENROLLING);
        }
    }
}
