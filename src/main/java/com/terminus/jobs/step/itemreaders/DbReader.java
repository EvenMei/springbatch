package com.terminus.jobs.step.itemreaders;

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

    private Integer page = 0;
    private Integer pageSize = 100;

    @Override
    protected void doReadPage() {
        setPageSize(pageSize);  // pageSize 的大小必须大于数据库中查询的数据量，否则无限循环查询
        if(results == null){
            results = new CopyOnWriteArrayList<>();
        }else{
            results.clear();
        }
        //动态的获取分页信息，一页一页的返回 limit a,b
        List<Student> studentList = studentMapper.findListPage(page*pageSize, pageSize);
        results.addAll(studentList);
        page++;
    }

    @Override
    protected void doJumpToPage(int i) {

    }
}
