package com.terminus.jobs.itemprocessors;

import com.terminus.pojo.Student;
import org.springframework.batch.item.ItemProcessor;

public class ChangeNameProcessor implements ItemProcessor<Student,Student> {
    @Override
    public Student process(Student student) throws Exception {
        student.setName(student.getName().toUpperCase());
        return student;
    }
}
