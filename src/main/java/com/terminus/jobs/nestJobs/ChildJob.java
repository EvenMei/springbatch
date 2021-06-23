package com.terminus.jobs.nestJobs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class ChildJob {

    @Bean
    public Step step1ofChildJob1(StepBuilderFactory stepBuilderFactory){
        return stepBuilderFactory.get("job1-step1").tasklet((stepContribution, chunkContext)-> {
            System.out.println("【Job1】RUNNING ====> step1");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public Job childJobOne(JobBuilderFactory jobBuilderFactory, Step step1ofChildJob1){
        return jobBuilderFactory.get("childJob1-01")
                .start(step1ofChildJob1)
                .build();
    }

}
