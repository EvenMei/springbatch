package com.terminus.jobs.itemreaders;

import com.terminus.mapper.StudentMapper;
import com.terminus.pojo.Student;
import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

//@Component
public class DbReader extends AbstractPagingItemReader<Student> {

    @Autowired
    private StudentMapper studentMapper;


    /*@Override
    protected Student doRead() throws Exception {

    }*/

    @Override
    protected void doReadPage() {
        setPageSize(200);  // pageSize 的大小必须大于数据库中查询的数据量，否则无限循环查询
        if(results == null){
            results = new CopyOnWriteArrayList<>();
        }else{
            results.clear();
        }
        List<Student> studentList = studentMapper.findList();
        results.addAll(studentList);
    }

    @Override
    protected void doJumpToPage(int i) {

    }
}
