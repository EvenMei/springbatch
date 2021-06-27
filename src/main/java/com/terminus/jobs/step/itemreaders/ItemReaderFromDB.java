package com.terminus.jobs.step.itemreaders;

import com.terminus.pojo.Student;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/*@Configuration
@EnableBatchProcessing*/
public class ItemReaderFromDB {

    @Bean
    public  ItemReader<Student> dbReader(){
        return new DbReader();
    };

    @Bean
    @StepScope  //bean 的生命周期和ItemReader 相同
    public ItemReader reader(DataSource datasource){
        JdbcPagingItemReader<Student> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(datasource);
//        reader.setFetchSize(0);
        reader.setRowMapper(getRowMapperFromResultSet());
        reader.setQueryProvider(getProvider());
        return  reader;
    }

    @Bean
    public ItemWriter writer(){
        return new DbWriter();
    }

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader dbReader, ItemWriter writer){
        return stepBuilderFactory.get("step1")
                .chunk(5)
                .reader(dbReader)
                .writer(writer)
                .build();
    }

    @Bean
    public Job job1(JobBuilderFactory jobBuilderFactory,Step step1){
        return jobBuilderFactory.get("itemReaderFromDB demo - "+ UUID.randomUUID())
                .start(step1)
                .build();
    }


    public RowMapper<Student> getRowMapperFromResultSet(){
        return (resultSet, i) ->{
            Student student = new Student();
            student.setId(resultSet.getInt("id"));
            student.setAddress(resultSet.getString("address"));
            student.setAge(resultSet.getInt("age"));
            student.setName(resultSet.getString("name"));
            return student;
        };
    }

    public PagingQueryProvider getProvider(){
        //指定sql语句
        MySqlPagingQueryProvider provider = new MySqlPagingQueryProvider();
        provider.setSelectClause("select id,address,age,name");
        provider.setFromClause("from student");
        //设置排序
        Map<String, Order> sortedMap = new HashMap<>(1);
        sortedMap.put("age",Order.DESCENDING);
        provider.setSortKeys(sortedMap);
        provider.setWhereClause("where age > 30");
        return provider;
    }

}
