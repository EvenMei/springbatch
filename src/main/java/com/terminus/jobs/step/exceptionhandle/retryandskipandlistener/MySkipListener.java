package com.terminus.jobs.step.exceptionhandle.retryandskipandlistener;

import com.terminus.pojo.Student;
import org.springframework.batch.core.SkipListener;

public class MySkipListener implements SkipListener<Student,Student> {

    @Override
    public void onSkipInRead(Throwable throwable) {
        System.out.println("exception detail is : " + throwable.getMessage().toUpperCase());
    }

    @Override
    public void onSkipInWrite(Student student, Throwable throwable) {
        System.out.println(" happened exception when write student  : " +student + "     exception msg : " + throwable.getMessage().toString());

    }

    @Override
    public void onSkipInProcess(Student student, Throwable throwable) {
        System.out.println(" happened exception when process student  : " +student + "     exception msg : " + throwable.getMessage().toString());

    }
}
