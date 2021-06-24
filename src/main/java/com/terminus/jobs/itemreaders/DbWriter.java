package com.terminus.jobs.itemreaders;

import com.terminus.pojo.Student;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class DbWriter implements ItemWriter<Student> {
    @Override
    public void write(List<? extends Student> list) throws Exception {

    }
}
