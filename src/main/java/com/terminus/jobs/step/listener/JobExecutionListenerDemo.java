package com.terminus.jobs.step.listener;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;

/*@Configuration
@EnableBatchProcessing*/
public class JobExecutionListenerDemo{

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory){
        return stepBuilderFactory.get("step1")
                .tasklet((stepContribution, chunkContext) -> {
                    System.out.println("RUNNING =====> step1");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Job myJob(JobBuilderFactory jobBuilderFactory,Step step1){
        return jobBuilderFactory.get("myJob-01")
                .start(step1)
                .listener(new MyJobListener())
                .build();
    }


}
