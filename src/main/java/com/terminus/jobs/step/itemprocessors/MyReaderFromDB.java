package com.terminus.jobs.step.itemprocessors;

import com.terminus.pojo.Student;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class MyReaderFromDB {

    public MyReaderFromDB(DataSource datasource) {
        this.datasource = datasource;
    }

    private DataSource datasource;

    public ItemReader<Student> obtainReader(){
        JdbcPagingItemReader<Student> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(datasource);
        reader.setQueryProvider(getMysqlQueryProvider());
        reader.setRowMapper(getRowMapper());
        return reader;
    }

    public MySqlPagingQueryProvider getMysqlQueryProvider(){
        MySqlPagingQueryProvider provider  = new MySqlPagingQueryProvider();
        provider.setSelectClause("select id, age,name,address");
        provider.setFromClause("from student");
        Map<String,Order> sortMap = new HashMap<String, Order>();
        sortMap.put("name",Order.DESCENDING);
        provider.setSortKeys(sortMap);
        return provider;
    }

    public RowMapper<Student> getRowMapper(){
        return (resultSet, i) -> {
            Student student = new Student();
            student.setId(resultSet.getInt("id"));
            student.setAge(resultSet.getInt("age"));
            student.setName(resultSet.getString("name"));
            student.setAddress(resultSet.getString("address"));
            return student;
        };
    }
}
