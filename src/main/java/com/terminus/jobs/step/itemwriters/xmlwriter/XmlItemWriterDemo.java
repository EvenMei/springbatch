package com.terminus.jobs.step.itemwriters.xmlwriter;

import com.terminus.jobs.step.itemwriters.MyReader;
import com.terminus.pojo.Student;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/*@Configuration
@EnableBatchProcessing*/
public class XmlItemWriterDemo {

    @Autowired
    private DataSource datasource;

    private Resource resource = new FileSystemResource("/Users/meiyukai/Desktop/mywriter.xml ");


    @Bean
    public ItemReader<Student> reader() {
        return new MyReader(datasource).obtainReader();
    }

    @Bean
    public ItemWriter<Student> writer() {
        StaxEventItemWriter<Student> swriter = new StaxEventItemWriter<>();
        swriter.setResource(resource);
        swriter.setRootTagName("Students");
        swriter.setMarshaller(getMarShaller());
        return swriter;
    }

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader reader, ItemWriter writer) {
        return stepBuilderFactory.get("step1")
                .chunk(2)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Bean
    public Job xmlItemWriterJob(JobBuilderFactory jobBuilderFactory, Step step1) {
        return jobBuilderFactory.get("xmlItemWriterJob - " + UUID.randomUUID())
                .start(step1)
                .build();
    }

    public Marshaller getMarShaller() {
        XStreamMarshaller marShaller = new XStreamMarshaller();
        Map<String, Class> typeMap = new HashMap<>();
        typeMap.put("studen", Student.class);  //fragmentRootElementName
        marShaller.setAliases(typeMap);
        return marShaller;
    }

}
