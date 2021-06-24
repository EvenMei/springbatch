package com.terminus.jobs.jobparameters;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Map;

@Configuration
@EnableBatchProcessing
public class ParametersDemo implements JobExecutionListener {

    private Map<String, JobParameter> parameters;

//    @Bean
    public ItemReader<String> getReader(){
//        return new MyReader();
        return new ListItemReader<String>(Arrays.asList(
                "from xiaomei",
                "from zhangsan",
                "from lisi"
//                parameters.get("name").toString()
        ));
    }

    @Bean
    public  ItemProcessor<String,String> processor(){
        return new MyProcessor();
    }

    @Bean
    public ItemWriter writer(){ return new MyWriter(); }



    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory,ItemWriter  writer, ItemProcessor<String,String>processor){
        return stepBuilderFactory.get("step1")
                .<String,String>chunk(2)
                .faultTolerant()
                .reader(getReader())
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean Step step2(StepBuilderFactory stepBuilderFactory){
        return stepBuilderFactory.get("step2")
                .tasklet((stepContribution , chunkContext) -> {
                    System.out.println("value = " + parameters.get("name"));
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Job myJobParameter(JobBuilderFactory jobBuilderFactory,Step step1,Step step2){
        return jobBuilderFactory.get("myJobParameter - 08")
                .start(step1)
                .listener(this)
                .build();
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("before job ======");
        this.parameters = jobExecution.getJobParameters().getParameters();
        System.out.println("name = " + parameters.get("name") );
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("after job ======");
//        parameters = jobExecution.getJobParameters().getParameters();
    }


}
