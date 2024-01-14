package com.project.devtogether.project.domain.enums;

import com.project.devtogether.common.error.ErrorCode;
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
                .orElseThrow(() -> new ApiException(ErrorCode.BAD_REQUEST, "존재하지 않는 입력값입니다."));
    }
}
