package com.terminus.jobs.exceptionhandle.retryandskip;

import com.terminus.pojo.Student;
import org.springframework.batch.item.ItemProcessor;

public class MyProcessor implements ItemProcessor<Student,Student> {
    private int retryTimes = 0;

    @Override
    public Student process(Student student) throws Exception {
        if(student.getId().equals(12)){
            if(retryTimes >=3){
                return student;
            }else{
                retryTimes++;
                System.out.println("retry times == " + retryTimes);
                throw new NullPointerException("ID EQUALS 12 ! STOPPED ! ");
            }
        }else if(student.getId().equals(14)){
            System.out.println("student id is  14 choose to skip");
            throw  new RuntimeException("STUDENT ID EQUALS 14 SKIPPED ! ");
        }
            return student;
    }
}
