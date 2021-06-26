package com.terminus.jobs.itemwriters.filereaders;

import com.terminus.jobs.itemwriters.MyReader;
import com.terminus.pojo.Student;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;
import java.util.UUID;

/**
 * 从数据中读取数据写入文件中
 * by <author>meiyukai</author>
 * at <Date>2021-06-26</Date>
 */

/*@Configuration
@EnableBatchProcessing*/
public class ItemFileWriterDemo {

    @Autowired
    private DataSource dataSource;

    /*@Value("classpath:itemfilewriter.txt")
    private Resource resource;*/

    private Resource resource = new FileSystemResource("/Users/meiyukai/Desktop/testwriter.txt");

    @Bean ItemReader<Student> reader(){
        return new MyReader(dataSource).obtainReader();
    }

    @Bean
    public ItemWriter<Student> writer(){
        FlatFileItemWriter<Student> fwriter = new FlatFileItemWriter<>();
        fwriter.setResource(resource);
        fwriter.setLineAggregator(getLineAggregator());
        return fwriter;
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
    public Job itemFileWriterJob(JobBuilderFactory jobBuilderFactory, Step step1){
        return jobBuilderFactory.get("itemFileWriterJob - " + UUID.randomUUID())
                .start(step1)
                .build();

    }

    public LineAggregator<Student> getLineAggregator(){
        return  student->{
            StringBuffer studentBuffer = new StringBuffer();
            studentBuffer.append(student.getId()).append(",")
                    .append(student.getAge()).append(",")
                    .append(student.getName()).append(",")
                    .append(student.getAddress());
            return studentBuffer.toString();
        };
    }


}
