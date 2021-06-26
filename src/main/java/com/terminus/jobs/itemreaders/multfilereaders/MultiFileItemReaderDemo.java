package com.terminus.jobs.itemreaders.multfilereaders;

import com.terminus.pojo.Student;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

import java.util.UUID;

/**
 * 多文件的读取实际上就是利用代理方式指定一个读取单一文件的reader去多次读取不同的文件
 *
 */
/*@Configuration
@EnableBatchProcessing*/
public class MultiFileItemReaderDemo {

    @Value("classpath*:student*.txt")
    private Resource[] resources;

    @Bean
    @StepScope
    public  MyWriter writer(){
        return new MyWriter();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Student> freader(){
        FlatFileItemReader<Student> freader = new FlatFileItemReader<>();
        freader.setLineMapper(getLineMapper());
        return freader;
    }

    @Bean
    @StepScope
    public MultiResourceItemReader<Student> mreader(FlatFileItemReader freader){
        MultiResourceItemReader<Student> mreader = new MultiResourceItemReader<>();
        mreader.setDelegate(freader);
        mreader.setResources(resources);
        return mreader;
    }

    @Bean
    public Step step1 (StepBuilderFactory stepBuilderFactory, ItemReader mreader, ItemWriter writer){
        return stepBuilderFactory.get("step1")
                .chunk(2)
                .reader(mreader)
                .writer(writer)
                .build();
    }

    @Bean
    public Job multiFileReaderJob(JobBuilderFactory jobBuilderFactory,Step step1){
        return jobBuilderFactory.get("multiFileReaderJob - "+ UUID.randomUUID())
                .start(step1)
                .build();
    }

    @Bean
    public LineMapper<Student> getLineMapper(){
        DefaultLineMapper<Student> defaultLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(new String[]{"id","age","name","address"});
        tokenizer.setDelimiter("@");  //不指定的情况下 默认用,(逗号)
        defaultLineMapper.setLineTokenizer(tokenizer);
        defaultLineMapper.setFieldSetMapper(getFieldSetMapper());
        return defaultLineMapper;
    }

    public FieldSetMapper<Student> getFieldSetMapper(){
        return fieldSet -> {
            Student student = new Student();
            student.setId(fieldSet.readInt("id"));
            student.setAge(fieldSet.readInt("age"));
            student.setName(fieldSet.readString("name"));
            student.setAddress(fieldSet.readString("address"));
            return student;
        };
    }

}
