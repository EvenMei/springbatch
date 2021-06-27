package com.terminus.jobs.step.itemreaders.multfilereaders;

import com.terminus.pojo.Student;
import org.springframework.batch.item.ItemWriter;

import java.util.List;


public class MyWriter implements ItemWriter<Student> {

    @Override
    public void write(List<? extends Student> list) throws Exception {
        System.out.println(" ----------> start to write");
        for(Student student : list){
            System.out.println("student :   "+ student);
        }
    }
}
