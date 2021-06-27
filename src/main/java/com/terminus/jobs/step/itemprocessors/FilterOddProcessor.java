package com.terminus.jobs.step.itemprocessors;

import com.terminus.pojo.Student;
import org.springframework.batch.item.ItemProcessor;

public class FilterOddProcessor implements ItemProcessor<Student,Student> {

    @Override
    public Student process(Student student) throws Exception {
        return (student.getId()&0x01)==0? student: null;
    }
}
