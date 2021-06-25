package com.terminus.jobs.itemreaders.filereaders;

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
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.validation.BindException;

import java.util.UUID;

/*@Configuration
@EnableBatchProcessing*/
public class FileItemReaderDemo {

    @Bean
    @StepScope
    public FlatFileItemReader<Student> reader(){
         FlatFileItemReader reader = new FlatFileItemReader<Student>();
//         reader.setResource(new FileSystemResource("/Users/meiyukai/Desktop/test.txt"));
         reader.setResource(new PathMatchingResourcePatternResolver().getResource("test.txt"));
//         reader.setLinesToSkip(1);  //暂时不需要跳过第一行
//        reader.setLineMapper(getLineMapper());
        reader.setLineMapper(getDefaultLineMapper());
         return reader;
    }

    @Bean
    @StepScope
    public ItemWriter<Student> writer(){
        return new MyFileItemWriter();
    }

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory,ItemReader reader, ItemWriter writer){
        return stepBuilderFactory.get("step1")
                .<Student,Student>chunk(2)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Bean
    public Job fileItemJob(JobBuilderFactory jobBuilderFactory,Step step1){
        return jobBuilderFactory.get("fileItemJob - "+ UUID.randomUUID())
                .start(step1)
                .build();
    }

    public LineMapper<Student> getLineMapper(){
        return (lineContent,lineNumber)->{
            Student student  = new Student();
            String[] rowString = lineContent.split(",");
            student.setId(null);
            student.setAge(Integer.parseInt(rowString[1])+100);
            student.setName(rowString[2]+"_test");
            student.setAddress(rowString[3]+"_test");
            return student;
        };
    }

    public FieldSetMapper<Student> getFieldMapper(){
        return new FieldSetMapper<Student>() {
            @Override
            public Student mapFieldSet(FieldSet fieldSet) throws BindException {
                Student student  = new Student();
                student.setAge(fieldSet.readInt("age"));
                student.setName(fieldSet.readString("name"));
                student.setAddress(fieldSet.readString("address"));
                return student;
            }
        };
    }

    public DefaultLineMapper<Student> getDefaultLineMapper(){
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(new String[]{"id","age","name","address"});

        DefaultLineMapper<Student> mapper = new DefaultLineMapper<>();
        mapper.setLineTokenizer(tokenizer);
        mapper.setFieldSetMapper(getFieldMapper());
        return mapper;
    }
}
