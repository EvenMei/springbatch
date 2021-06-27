package com.terminus.jobs.step.itemwriters.multifilewriters;

import com.alibaba.fastjson.JSON;
import com.terminus.pojo.Student;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.core.io.Resource;

public class MyFileWriter {

    private Resource resource;

    public MyFileWriter(Resource resource) {
        this.resource = resource;
    }

    public FlatFileItemWriter<Student> obtainWriter(){
        FlatFileItemWriter<Student> writer = new FlatFileItemWriter<>();
        writer.setResource(resource);
        writer.setLineAggregator(getLineAggregator());
        return writer;
    }

    public LineAggregator<Student> getLineAggregator(){
        return student->{
            String studentJsonStr = JSON.toJSONString(student);
            return studentJsonStr;
        };
    }
}
