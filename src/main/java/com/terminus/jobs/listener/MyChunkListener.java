package com.terminus.jobs.listener;

import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.scope.context.ChunkContext;

public class MyChunkListener{

    @BeforeChunk
    public void before(/*StepContribution contribution ,*/ ChunkContext context){
        System.out.println(" 【Chunk Before 】"/*+contribution.getStepExecution().getStepName()+" isCompleted ?   ===>"*/+context.isComplete());
    }

    @AfterChunk
    public void after(/*StepContribution  contribution , */ChunkContext context){
        System.out.println(" 【Chunk After 】"/*+contribution.getStepExecution().getStepName()+" isCompleted ?   ===>"*/+context.isComplete());
    }

}
