package com.project.devtogether.common.redis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RedisCache {
    MEMBER_REDIS_KEY("member::"),
    PROJECT_REDIS_KEY("project::"),
    REDIS_DURATION("60"),
    ACCESS_BLACKLIST_DURATION("24");

    private String value;
}
