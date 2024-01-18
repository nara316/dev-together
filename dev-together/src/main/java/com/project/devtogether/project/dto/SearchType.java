package com.project.devtogether.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SearchType {
    TITLE("제목"),
    NICKNAME("작성자"),
    SKILL("사용스킬");

    private final String description;

}
