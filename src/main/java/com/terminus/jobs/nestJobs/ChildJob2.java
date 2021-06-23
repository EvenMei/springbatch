package com.terminus.jobs.nestJobs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;

/*@Configuration
@EnableBatchProcessing*/
public class ChildJob2 {

    @Bean
    public Step step1ofChildJob2(StepBuilderFactory stepBuilderFactory){
        return stepBuilderFactory.get("job2-step1").tasklet((stepContribution, chunkContext)-> {
            System.out.println("【Job2】RUNNING ====> step1");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public Step step2ofChildJob2(StepBuilderFactory stepBuilderFactory){
        return stepBuilderFactory.get("obtain job2 step2").tasklet((stepContribution, chunkContext)-> {
            System.out.println("【Job2】RUNNING ====> step2");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public Job childJobTwo(JobBuilderFactory jobBuilderFactory, Step step1ofChildJob2, Step step2ofChildJob2){
        return jobBuilderFactory.get("childJob2-01")
                .start(step1ofChildJob2)
                .next(step2ofChildJob2)
                .build();
    }

}
