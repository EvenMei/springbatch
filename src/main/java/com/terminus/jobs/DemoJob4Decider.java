package com.terminus.jobs;

import com.terminus.jobs.deciders.MyDecider;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class DemoJob4Decider {

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory){
        return stepBuilderFactory.get("step1").tasklet((stepContribution, chunkContext) ->{
            System.out.println("RUNNING ====> step1");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public Step step2(StepBuilderFactory stepBuilderFactory){
        return stepBuilderFactory.get("step2").tasklet((stepContribution, chunkContext) ->{
            System.out.println("RUNNING ====> step2");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public Step step3(StepBuilderFactory stepBuilderFactory){
        return stepBuilderFactory.get("step3").tasklet((stepContribution, chunkContext) ->{
            System.out.println("RUNNING ====> step3");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public JobExecutionDecider jobExecutionDecider(){
        return new MyDecider();
    }

    @Bean
    public Job deciderJob(JobBuilderFactory jobBuilderFactory,JobExecutionDecider jobExecutionDecider, Step step1, Step step2,Step step3) {
        return jobBuilderFactory.get("jobExecutionDecider - demo 03")
                .start(step1)
                /*.next(jobExecutionDecider).on("odd").to(step3)*/
                .next(jobExecutionDecider).on("even").to(step2)
                .next(jobExecutionDecider).on("odd").to(step3)
                .from(step3).on("*").to(jobExecutionDecider)
                .end().build();

        /*return jobBuilderFactory.get("jobExecutionDemo 02")
                .start(step1)
                .next(jobExecutionDecider)
                .from(jobExecutionDecider).on("even").to(step2)
                .from(jobExecutionDecider).on("odd").to(step3)
                .from(step3).on("*").to(jobExecutionDecider)
                .from(step2).on("*").to(jobExecutionDecider)
                .end().build();*/

    }
}
