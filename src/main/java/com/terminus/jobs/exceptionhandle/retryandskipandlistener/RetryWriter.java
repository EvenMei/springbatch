package com.terminus.jobs.exceptionhandle.retryandskipandlistener;

import com.terminus.pojo.Student;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class RetryWriter implements ItemWriter<Student> {
    @Override
    public void write(List<? extends Student> list) throws Exception {
        System.out.println(" ----->  start to writ");
        for (Student student : list){
            System.out.println("student : " + student);
        }
    }
}
