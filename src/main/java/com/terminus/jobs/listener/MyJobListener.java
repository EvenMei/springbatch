package com.terminus.jobs.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * 使用接口的方式实现监听
 * 还可以使用注解的方式实现监听
 */
public class MyJobListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("before job start  ====>" + jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("after job start ====> " +jobExecution.getJobInstance().getJobName());
    }
}
