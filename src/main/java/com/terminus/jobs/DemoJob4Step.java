package com.terminus.jobs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*@Configuration
@EnableBatchProcessing*/
public class DemoJob4Step {

   /* @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;*/

    @Bean
    public Job demoJob(JobBuilderFactory jobBuilderFactory , Step step1 , Step step2, Step step3){
//        return jobBuilderFactory.get("other demoJob4").start(step1).next(step2).build();
        return jobBuilderFactory.get("demo job 2")
                .start(step1).on("COMPLETED").to(step2)   //stop //stopAndRestart // fail
                .from(step2).on("COMPLETED").to(step3)
                .from(step3).end().build();
    }

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory){
        return stepBuilderFactory.get("start step1").tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                System.out.println("RUNNING ====> STEP-1");
                return RepeatStatus.FINISHED;
            }
        }).build();
    }

    @Bean
    public Step step2(StepBuilderFactory stepBuilderFactory){
        return stepBuilderFactory.get("start step2").tasklet((StepContribution stepContribution, ChunkContext chunkContext) -> {
            System.out.println("RUNNING ==== STEP-2");
            return RepeatStatus.FINISHED;
        }).build();
    }
    @Bean
    public Step step3(StepBuilderFactory stepBuilderFactory){
        return stepBuilderFactory.get("start step3").tasklet((StepContribution stepContribution, ChunkContext chunkContext) -> {
            System.out.println("RUNNING ==== STEP-3");
            return RepeatStatus.FINISHED;
        }).build();
    }

}
