package com.terminus.config;

import com.terminus.jobs.step.exceptionhandle.readerException.MyWriter;
import com.terminus.jobs.step.exceptionhandle.retryandskipandlistener.MyReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

//@Configuration
//@EnableBatchProcessing
public class JobLauncherConfig {

    @Bean
    public ItemReader reader(DataSource dataSource){
        return new MyReader(dataSource).obtainReader();
    }

    @Bean
    public ItemWriter writer(){
        return new MyWriter();
    }

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader reader, ItemWriter writer){
        return stepBuilderFactory.get("step1")
                .chunk(2)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Bean
    public Job launcherJob(JobBuilderFactory jobBuilderFactory, Step step1){
        return jobBuilderFactory.get("launcherJob")
                .start(step1)
                .build();
    }

}
