package com.terminus.jobs.exceptionhandle.retryandskip;

import com.terminus.pojo.Student;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.UUID;

@Configuration
@EnableBatchProcessing
public class RetryAndSkipStepDemo {

   @Bean
   public ItemReader reader(DataSource dataSource){
       return new MyReader(dataSource).obtainReader();
   }

   @Bean
   public ItemProcessor<Student,Student> processor(){
       return new MyProcessor();
   }
   
   @Bean
   public ItemWriter writer(){
       return new RetryWriter();
   }

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader reader, ItemProcessor processor,ItemWriter writer){
        return stepBuilderFactory.get("step1")
                .<Student, Student>chunk(2)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .retry(NullPointerException.class)
                .retryLimit(5)// reader and writer and processor 中的失败次数总和 不超过5
                .skip(RuntimeException.class)
                .skipLimit(5)
                .build();
    }

    @Bean
    public Job retryStepJob(JobBuilderFactory jobBuilderFactory, Step step1){
        return jobBuilderFactory.get("retryStepJob - " + UUID.randomUUID() )
                .start(step1)
                .build();
    }



}
