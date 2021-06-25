package com.terminus.mapper;

import com.terminus.pojo.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class StudentMapperTest {

    @Autowired
    private StudentMapper studentMapper;

    @Test
    public void findList() {
        List<Student> studentList = studentMapper.findList();
        System.out.println("studentList = " + studentList);
    }

    @Test
    public void addStudent(){
        Student student  = new Student();
        student.setAge(22);
        student.setName("mmmmm_mmmmm");
        student.setAddress("yuewanglu");
        int affectCount = studentMapper.addStudent(student);
       /* Map<String,Object> studentMap = new HashMap<>();
        studentMap.put("name","zhangshandegea");
        studentMap.put("age",99);
        studentMap.put("address","nanjing road");
        int affectCount = studentMapper.addStudent2(studentMap);*/
        System.out.println("affectCount = " + affectCount);
    }
    
}