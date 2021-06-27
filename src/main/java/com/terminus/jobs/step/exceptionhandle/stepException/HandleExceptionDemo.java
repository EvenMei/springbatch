package com.terminus.jobs.step.exceptionhandle.stepException;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;

/**
 * 默认情况下  遇到异常的时候，SpringBatch 会结束任务，
 * 当使用相同的参数重启时，SpringBatch会去执行未执行的剩余任务
 */
/*@Configuration
@EnableBatchProcessing*/
public class HandleExceptionDemo {

    @Bean
    public Tasklet tasklet(){
        return (stepContribution, chunkContext) ->{
            ExecutionContext stepExecutionContext = chunkContext.getStepContext().getStepExecution().getExecutionContext();
            String stepName = chunkContext.getStepContext().getStepName();
            if(stepExecutionContext.containsKey("runtimes")){
                System.out.println("second run can be success !"  + "    step name is :  ----->    "+stepName);
                return RepeatStatus.FINISHED;
            }else{
                System.out.println("first run will be fail !" +  "    step name is :  ----->    "+stepName);
//                stepExecutionContext.put("runtimes",1);
                chunkContext.getStepContext().getStepExecution().getExecutionContext().putLong("runtimes",1L);
                throw new RuntimeException("FIRST RUN FAILED ！" + "    step name is :  ----->    "+stepName);
            }
        };
    }

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, Tasklet tasklet){
        return stepBuilderFactory.get("step1")
                .tasklet(tasklet)
                .build();
    }

    @Bean
    public Step step2(StepBuilderFactory stepBuilderFactory, Tasklet tasklet){
        return stepBuilderFactory.get("step2")
                .tasklet(tasklet)
                .build();
    }

    @Bean
    public Job exceptionJob(JobBuilderFactory jobBuilderFactory, Step step1, Step step2){
        return jobBuilderFactory.get("exceptionJob - 1")
                .start(step1)
                .next(step2)
                .build();
    }


}
