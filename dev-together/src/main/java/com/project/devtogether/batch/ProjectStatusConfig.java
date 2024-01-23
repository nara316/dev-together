package com.project.devtogether.batch;

import com.project.devtogether.project.domain.Project;
import com.project.devtogether.project.domain.enums.ProjectStatus;
import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class ProjectStatusConfig {

    private final int CHUNK_SIZE = 100;

    private final EntityManagerFactory entityManagerFactory;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Job projectStatusJob() {
        return new JobBuilder("projectStatusJob", jobRepository)
                .start(projectStatusStep())
                .build();
    }

    @Bean
    public Step projectStatusStep() {
        return new StepBuilder("projectStatusStep", jobRepository)
                .<Project, Project>chunk(CHUNK_SIZE, transactionManager)
                .reader(projectStatusItemReader())
                .processor(projectStatusItemProcessor())
                .writer(projectStatusItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public JpaCursorItemReader<Project> projectStatusItemReader() {
        return new JpaCursorItemReaderBuilder<Project>()
                .name("projectStatusItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select p from Project p "
                        + "where p.status = :status and p.advertiseEndDate <= :advertiseEndDate")
                .parameterValues(Map.of("status", ProjectStatus.ENROLLING, "advertiseEndDate", LocalDateTime.now()))
                .build();
    }

    @Bean
    public ItemProcessor<Project, Project> projectStatusItemProcessor() {
        return project -> {
            project.setStatus(ProjectStatus.CLOSED);
            return project;
        };
    }

    @Bean
    public JpaItemWriter<Project> projectStatusItemWriter() {
        return new JpaItemWriterBuilder<Project>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}
