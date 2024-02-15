package com.project.devtogether;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableBatchProcessing
@EnableScheduling
@EnableCaching
@SpringBootApplication
public class DevTogetherApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevTogetherApplication.class, args);
    }
}
