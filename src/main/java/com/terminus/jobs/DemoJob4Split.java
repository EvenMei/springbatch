package com.terminus.jobs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

/**
 *
 * this demo used to learn about <hint>springbatch</hint> <link>split</link>
 *by author <author>meiyukai</author> at <date>2021.06.21</date>
 */
/*@Configuration
@EnableBatchProcessing*/
public class DemoJob4Split {

    @Bean Step splitStep1(StepBuilderFactory stepBuilderFactory){
        return stepBuilderFactory.get("splitStep1").tasklet((stepContribution, chunkContext) -> {
            System.out.println("RUNNING =====> step1");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean Step splitStep2(StepBuilderFactory stepBuilderFactory){
        return stepBuilderFactory.get("splitStep2").tasklet((stepContribution, chunkContext) -> {
            System.out.println("RUNNING =====> step2");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean Step splitStep3(StepBuilderFactory stepBuilderFactory){
        return stepBuilderFactory.get("splitStep3").tasklet((stepContribution, chunkContext) -> {
            System.out.println("RUNNING =====> step3");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public Flow splitFlow(Step splitStep1){
        return new FlowBuilder<Flow>("FlowBuilder4Split-1").start(splitStep1).build();
    }

    @Bean
    public Flow splitFlow2(Step splitStep2, Step splitStep3){
        return new FlowBuilder<Flow>("FlowBuilder4Split-2")
                .start(splitStep2).on("COMPLETED").to(splitStep3)
                .from(splitStep3).on("COMPLETED").end().build();
    }

    @Bean
    public Job splitJob(JobBuilderFactory jobBuilderFactory, Flow splitFlow , Flow splitFlow2){
        return jobBuilderFactory.get("splitJobFactory")
                .start(splitFlow)
                .split(new SimpleAsyncTaskExecutor()).add(splitFlow2)
                .end().build();
    }


}
