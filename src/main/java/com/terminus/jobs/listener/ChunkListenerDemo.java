package com.terminus.jobs.listener;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

/*@Configuration
@EnableBatchProcessing*/
public class ChunkListenerDemo {

    @Bean
    public ItemReader<String> read(){
        return new ListItemReader<String>(Arrays.asList("zhangsan","lisi","wangwu"));
    }

    @Bean
    public ItemWriter<String> write(){
        return list -> {
            for(String name : list){
                System.out.println("writer write : " + name);
            }
        };
    }

    @Bean
    public ItemProcessor<String,String>processor(){
        return new MyProcessor();
    }


    @Bean
    public Step step1 (StepBuilderFactory stepBuilderFactory, ItemReader read, ItemWriter write,ItemProcessor processor){
        return stepBuilderFactory.get("step1")
                .<String,String>chunk(2)  // 每read  完2个数据进行一次输出
                .faultTolerant() // 容错处理
                .listener(new MyChunkListener())
                .reader(read)
                .processor(processor)
                .writer(write)
                .build();
    }

    @Bean
    public Job myJob2(JobBuilderFactory jobBuilderFactory, Step step1){
        return jobBuilderFactory.get("myJob2-04")
                .start(step1)
                .listener(new MyJobListener())
                .build();

    }
}
