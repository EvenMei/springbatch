package com.terminus.jobs.itemreaders.xmlreaders;

import com.terminus.pojo.Student;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/*@Configuration
@EnableBatchProcessing*/
public class XmlItemReaderDemo implements JobExecutionListener {
    private Map<String, JobParameter> parameters;

    /**s
     * @StepScope 这是是一个坑 ：@StepScope 注解的对象生命周期和Step一样，Job启动时 @StepScope注解的对象reader才开始进行初始化
     *                                                  导致一个问题，容器中并没有一个现存的实例，于是spring尝试使用一个具体class的代理对象，而此时是一个接口ItemReader
     *                                                  并没有指定是什么具体的类型导致，createBeanException
     *                                                  但是如果将ItemReader ---> 具体的对象实例 l例如 StaxEventItemReader<T> spring会使用代理模式先注入一个改类型的对象
     *  建议：1. 使用@StepScope 的时候需要指明具体的ItemReader<T>
     *            2. 配合@Value(""#{jobParameters[key]}) （若没有此项就不必使用@StepScope）
     * @return
     */
    @Bean
    @StepScope
    public StaxEventItemReader<Student> reader(/*@Value("#{jobParameters[name]}") String value*/) {
//        System.out.println("value = "+value);
        StaxEventItemReader<Student> stackreader = new StaxEventItemReader<>();
        Resource resource = new PathMatchingResourcePatternResolver().getResource("student.xml");
//        reader.setResource(new PathMatchingResourcePatternResolver().getResource("student.xml"));
        stackreader.setResource(resource);
        stackreader.setFragmentRootElementName("student");
        stackreader.setUnmarshaller(getXStreamMarshaller());
        return stackreader;
    }

    @Bean
    @StepScope
    public XmlWriter<Student> writer(){
        return new XmlWriter();
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
    public Job xmlItemReaderJob(JobBuilderFactory jobBuilderFactory,Step step1){
        return jobBuilderFactory.get("xmlItemReaderJob - demo -"+ UUID.randomUUID())
                .start(step1)
                .build();
    }

    public XStreamMarshaller getXStreamMarshaller(){
        XStreamMarshaller unMarshaller = new XStreamMarshaller();
        Map<String,Class> typeMap = new HashMap<>();
        typeMap.put("student",Student.class);
        unMarshaller.setAliases(typeMap);
        return unMarshaller;
    }


    @Override
    public void beforeJob(JobExecution jobExecution) {
        parameters = jobExecution.getJobParameters().getParameters();
        System.out.println("parameters = "+ parameters);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {

    }
}
