package com.terminus.jobs.itemreaders;

import com.terminus.jobs.jobparameters.MyWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

/*@Configuration
@EnableBatchProcessing*/
public class ItemReaderJobDemo {

    @Bean
    public ItemReader<String> reader(){
        return new MyReader(Arrays.asList("a","b","c","d","e"));
    }

    @Bean
    public ItemWriter<String>writer(){
        return new MyWriter();
    }

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory,ItemReader reader, ItemWriter writer){
        return stepBuilderFactory.get("step1")
                .<String,String>chunk(3)
                .faultTolerant()
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Bean
    public Job job1(JobBuilderFactory jobBuilderFactory, Step step1){
        return jobBuilderFactory.get("job1 -times-4")
                .start(step1)
                .build();
    }



}
