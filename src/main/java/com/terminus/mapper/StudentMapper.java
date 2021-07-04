package com.terminus.mapper;

import com.terminus.pojo.Student;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface StudentMapper {

    List<Student> findList();

    List<Student> findListPage(Integer start, Integer size);

    int addStudent(Student student);

    int addStudent2(Map<String,Object> studentMap);

    int addList(List<Student> studentList );

}