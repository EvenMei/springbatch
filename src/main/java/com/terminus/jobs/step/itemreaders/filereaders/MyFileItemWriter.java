package com.terminus.jobs.step.itemreaders.filereaders;

import com.terminus.mapper.StudentMapper;
import com.terminus.pojo.Student;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MyFileItemWriter implements ItemWriter<Student> {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public void write(List<? extends Student> list) throws Exception {
        System.out.println("start to save student into db");
        for(Student student : list){
            studentMapper.addStudent(student);
        }
        System.out.println(" save students done! ");
    }
}
