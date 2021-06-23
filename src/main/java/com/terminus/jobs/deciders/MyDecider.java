package com.terminus.jobs.deciders;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
@Slf4j
public class MyDecider implements JobExecutionDecider {
    private int count = 0 ;
    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        count++;
        if((count & 0x01) == 0){
            if(log.isDebugEnabled()){
                log.warn("MyDecider   even  jobExecution = {}" , jobExecution.toString() );
            }
            return new FlowExecutionStatus("even");
        }else{
            if(log.isDebugEnabled()){
                log.warn("MyDecider odd job  stepExecution = {}" , stepExecution);
            }
            return new FlowExecutionStatus("odd");
        }
    }
}
