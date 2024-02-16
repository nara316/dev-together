package com.project.devtogether.common.redis.config;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.devtogether.common.error.RedisErrorCode;
import com.project.devtogether.common.exception.ApiException;
import com.project.devtogether.common.redis.service.RedisService;
import java.time.Duration;
import java.util.Collection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ObjectSerializer {

    private final RedisService redisService;

    public <T> void saveData(String key, T data, int minutes) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String value = mapper.writeValueAsString(data);
            redisService.setValues(key, value, Duration.ofMinutes(minutes));
        } catch (Exception e) {
            throw new ApiException(RedisErrorCode.CACHE_SAVE_FAIL);
        }
    }

    public <T> Optional<T> getData(String key, Class<T> classType) {
        String value = redisService.getValues(key);
        if (value.equals("false")) {
            return Optional.empty();
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            return Optional.of(mapper.readValue(value, classType));
        } catch (Exception e) {
            throw new ApiException(RedisErrorCode.CACHE_NOT_EXIST);
        }
    }

    public <T> Optional<T> getDataList(String key, Class<?> collectionType, Class<?> elementType) {
        String value = redisService.getValues(key);
        if (value.equals("false")) {
            return Optional.empty();
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            JavaType type = mapper
                    .getTypeFactory()
                    .constructCollectionType((Class<? extends Collection<?>>) collectionType, elementType);
            return Optional.of(mapper.readValue(value, type));
        } catch (Exception e) {
            throw new ApiException(RedisErrorCode.CACHE_NOT_EXIST);
        }
    }
}
