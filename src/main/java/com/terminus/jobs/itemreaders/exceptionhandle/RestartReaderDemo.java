package com.terminus.jobs.itemreaders.exceptionhandle;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

/*@Configuration
@EnableBatchProcessing*/
public class RestartReaderDemo {

    @Value("classpath:restart.txt")
    private Resource resource;

    @Bean
    public RestartReader reader(){
        return new RestartReader(resource);
    }

    @Bean
    public MyWriter writer(){
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
    public Job restartReaderJob(JobBuilderFactory jobBuilderFactory, Step step1){
        return jobBuilderFactory.get("restartReaderJob -  12"/*+ UUID.randomUUID()*/)
                .start(step1)
                .build();
    }
}
