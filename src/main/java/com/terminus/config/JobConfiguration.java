package com.terminus.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
@EnableBatchProcessing
public class JobConfiguration {
    // 注入创建任务对象的对象
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    // 任务的执行由step决定
    //注入创建step的对象
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job myJob(){
        return jobBuilderFactory.get("get taskName")
                .start(myStep1())
                .build();
    }

    @Bean
    public Step myStep1(){
        return stepBuilderFactory.get("step1 name").tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                System.out.println("myStep1 printTime ====> "+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                return RepeatStatus.FINISHED;
            }
        }).build();
    }

}
