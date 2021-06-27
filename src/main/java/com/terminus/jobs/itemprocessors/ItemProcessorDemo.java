package com.terminus.jobs.itemprocessors;

import com.terminus.pojo.Student;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * ItemProcessor 的使用有两种方式：
 * 第一直接调用.processor()
 * 第二使用 CompositeItemProcessor()
 * job 中使用多个processor的时候直接 .Processor(xxxxProcessor)
 */
/*@Configuration
@EnableBatchProcessing*/
public class ItemProcessorDemo {

    @Bean
    public ItemReader reader(DataSource datasource){
        return new MyReaderFromDB(datasource).obtainReader();
    }


    @Bean
    public ItemProcessor<Student,Student> changeNameProcessor(){
        return new ChangeNameProcessor();
    }

    @Bean
    public ItemProcessor<Student,Student> filterOddProcessor(){
        return new FilterOddProcessor();
    }

    @Bean
    public CompositeItemProcessor<Student,Student> compositeItemProcessor(ItemProcessor changeNameProcessor, ItemProcessor filterOddProcessor){
        CompositeItemProcessor<Student,Student> compositeItemProcessor = new CompositeItemProcessor<>();
        List<ItemProcessor<Student,Student>> itemProcessorList = new ArrayList<>();
        itemProcessorList.add(changeNameProcessor);
        itemProcessorList.add(filterOddProcessor);
        compositeItemProcessor.setDelegates(itemProcessorList);
        return compositeItemProcessor;
    }


    @Bean
    public ItemWriter writer(){
        return new MyWriter();
    }


    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader reader, CompositeItemProcessor compositeItemProcessor,ItemProcessor<Student,Student>  changeNameProcessor, ItemProcessor<Student,Student> filterOddProcessor ,ItemWriter writer){
        return stepBuilderFactory.get("step1")
                .<Student,Student>chunk(2)
                .reader(reader)
           /*     .processor(changeNameProcessor)
                .processor(filterOddProcessor)*/
                .processor(compositeItemProcessor)
                .writer(writer)
                .build();

    }

    @Bean
    public Job itemProcessorJob(JobBuilderFactory jobBuilderFactory, Step step1){
        return jobBuilderFactory.get("itemProcessorJob - "+ UUID.randomUUID())
                .start(step1)
                .build();
    }
}
