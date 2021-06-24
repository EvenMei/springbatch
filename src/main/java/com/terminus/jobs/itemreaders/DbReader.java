package com.terminus.jobs.itemreaders;

import com.terminus.pojo.Student;
import org.springframework.batch.item.database.JdbcPagingItemReader;

public class DbReader extends JdbcPagingItemReader<Student> {

}
