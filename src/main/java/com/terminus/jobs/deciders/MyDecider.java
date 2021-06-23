package com.terminus.jobs.deciders;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

public class MyDecider implements JobExecutionDecider {
    private int count = 0 ;
    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        count++;
        if((count & 0x01) == 0){
            return new FlowExecutionStatus("even");
        }else{
            return new FlowExecutionStatus("odd");
        }
    }
}
