package com.terminus.jobs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class DemoJob4Flow {

    @Bean
    public Job flowJob(JobBuilderFactory jobBuilderFactory, Flow stepFlow,Step step3){
        return jobBuilderFactory.get("demo 4 flow")
                .start(stepFlow)
                .next(step3)
                .end().build();
    }

    @Bean
    public Step flowStep1(StepBuilderFactory stepBuilderFactory){
        return stepBuilderFactory.get("flow step 1").tasklet((stepContribution,chunkContext)->{
            System.out.println("RUNNING ====> flow step 1");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public Step flowStep2(StepBuilderFactory stepBuilderFactory){
        return stepBuilderFactory.get("flow step 2").tasklet((stepContribution,chunkContext)->{
            System.out.println("RUNNING ====> flow step 2");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public Step flowStep3(StepBuilderFactory stepBuilderFactory){
        return stepBuilderFactory.get("flow step 3").tasklet((stepContribution,chunkContext)->{
            System.out.println("RUNNING ====> flow step 3");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public Flow stepFlow(Step step1, Step step2){
        return new FlowBuilder<Flow>("FlowBuilder")
                .start(step1).on("COMPLETED").to(step2)
                .build();
    }

}
