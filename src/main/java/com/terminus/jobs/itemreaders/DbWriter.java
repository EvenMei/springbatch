package com.terminus.jobs.itemreaders;

import com.terminus.pojo.Student;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class DbWriter implements ItemWriter<Student> {
    @Override
    public void write(List<? extends Student> list) throws Exception {
        System.out.println("list size is :     " + list.size());
        for (Student student : list){
            System.out.println("【writer】 ---->  " + student);
        }
    }
}
