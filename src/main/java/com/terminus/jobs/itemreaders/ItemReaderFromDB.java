package com.terminus.jobs.itemreaders;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class ItemReaderFromDB {

    @Bean
    @StepScope
    public ItemReader reader(){
        return new DbReader();
    }

    @Bean
    public ItemWriter writer(){
        return new DbWriter();
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
    public Job job1(JobBuilderFactory jobBuilderFactory,Step step1){
        return jobBuilderFactory.get("itemReaderFromDB demo - 01")
                .start(step1)
                .build();
    }


}
