package com.terminus.jobs.exceptionhandle.retryandskip;

import com.terminus.pojo.Student;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class MyReader {

    private DataSource datasource;

    public MyReader(DataSource datasource) {
        this. datasource = datasource;
    }



    public ItemReader obtainReader(){
        JdbcPagingItemReader<Student> jdbcReader = new JdbcPagingItemReader<>();
        jdbcReader.setDataSource(datasource);
        jdbcReader.setRowMapper(getRowMapper());
        jdbcReader.setQueryProvider(getProvider());
        return jdbcReader;
    }

    public RowMapper<Student> getRowMapper(){
        return (resultSet, i) -> {
            Student student = new Student();
            student.setId(resultSet.getInt("id"));
            student.setAge(resultSet.getInt("age"));
            student.setName(resultSet.getString("name"));
            student.setAddress(resultSet.getString("address"));
            return  student;
        };
    }

    public MySqlPagingQueryProvider getProvider(){
        MySqlPagingQueryProvider provider = new MySqlPagingQueryProvider();
        provider.setSelectClause("select id,age,name,address");
        provider.setFromClause("from student");
        Map<String, Order> orderMap = new HashMap<>();
        orderMap.put("age",Order.ASCENDING);
        provider.setSortKeys(orderMap);
        return provider;
    }
}
