package com.terminus.jobs.itemwriters.multifilewriters;

import com.terminus.jobs.itemwriters.MyReader;
import com.terminus.pojo.Student;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;

import javax.sql.DataSource;
import java.util.*;

/**
 * 从数据库中取数据写入不同的文件
 * 运行成功后写入到了 /[projectPath]/target/write1.txt    /[projectPath]/target/write2.xml
 */

@Configuration
@EnableBatchProcessing
public class MultiFileWriterDemo {

    @Value("classpath:write1.txt")
    private Resource resources4txt;

    @Value("classpath:write2.xml")
    private Resource resources4xml;


    @Bean
    public  ItemReader reader(DataSource datasource){
        return new MyReader(datasource).obtainReader();
    }

    @Bean
    public StaxEventItemWriter<Student> xmlWriter(){
        StaxEventItemWriter<Student> writer = new StaxEventItemWriter<>();
        writer.setResource(resources4xml);
        writer.setMarshaller(getMarShaller());
        writer.setRootTagName("Students");
        return writer;
    }

    @Bean
    public FlatFileItemWriter<Student> txtWriter(){
        return new MyFileWriter(resources4txt).obtainWriter();
    }

    /**
     * 第一种多文件批量写入方式   ---> CompositeItemWriter
     * @param xmlWriter
     * @return
     */
    @Bean
    public ItemWriter compositeWriter(StaxEventItemWriter xmlWriter, FlatFileItemWriter<Student> txtWriter){

        CompositeItemWriter<Student> cwriter = new CompositeItemWriter<>();
        List<ItemWriter<? super Student>>itemWriterList = new ArrayList<>();
        itemWriterList.add(txtWriter);
        itemWriterList.add(xmlWriter);
        cwriter.setDelegates(itemWriterList);
        return cwriter;
    }


    /**
     * 第二种方式 按类型分类的ItemWriter
     */
    @Bean
    public ClassifierCompositeItemWriter<Student> classifierWriter(StaxEventItemWriter xmlWriter, FlatFileItemWriter txtWriter){
        ClassifierCompositeItemWriter writer = new ClassifierCompositeItemWriter();
        writer.setClassifier(getClassifier(xmlWriter,txtWriter));
        return writer;
    }


    @Bean
    public  Step step1(StepBuilderFactory stepBuilderFactory, ItemReader reader, ItemWriter compositeWriter , ClassifierCompositeItemWriter classifierWriter ,StaxEventItemWriter xmlWriter, FlatFileItemWriter txtWriter){
        return stepBuilderFactory.get("step1")
                .chunk(2)
                .reader(reader)
                .writer(classifierWriter)
                .stream(xmlWriter)
                .stream(txtWriter)
                .build();
    }

    @Bean
    public Job multiFileItemWriterJob(JobBuilderFactory jobBuilderFactory, Step step1){
        return jobBuilderFactory.get("jobBuilderFactory - "+ UUID.randomUUID())
                .start(step1)
                .build();
    }


    public Marshaller getMarShaller(){
        XStreamMarshaller marshaller = new XStreamMarshaller();
        Map<String, Class> aliasesMap = new HashMap<>();
        aliasesMap.put("student",Student.class);  //fragmentRootElementName
        marshaller.setAliases(aliasesMap);
        return marshaller;
    }

    public Classifier<Student, ItemWriter<? super Student>>  getClassifier(StaxEventItemWriter xmlWriter, FlatFileItemWriter txtWriter){
        return student->{
            if((student.getId()& 0x01) == 0){
                return txtWriter;
            }else{
                return xmlWriter;
            }
        };
    }


}
