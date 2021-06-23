package com.terminus.jobs.nestJobs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.JobStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class ParentJob {


    /**
     * 这种启动方式需要指定启动方式：
     *spring.batch.job.names=parentJob
     * @param jobBuilderFactory
     * @param jobStep1 子job1
     * @param jobStep2 子job2
     * @return Job类型的fu父Job
     * */
    @Bean
    public Job myParentJob(JobBuilderFactory jobBuilderFactory,Step jobStep1, Step jobStep2){
        return jobBuilderFactory.get("parentJob-01")
                .start(jobStep1)
                .next(jobStep2)
                .build();
    }

    @Bean
    public Step jobStep1(Job childJobOne, JobLauncher jobLauncher , JobRepository jobRepository, PlatformTransactionManager manager){
        return new JobStepBuilder(new StepBuilder("childJob-01"))
                .job(childJobOne)
                .launcher(jobLauncher)
                .repository(jobRepository)
                .transactionManager(manager)
                .build();
    }

    @Bean
    public Step jobStep2(Job childJobTwo, JobLauncher jobLauncher, JobRepository jobRepository, PlatformTransactionManager manager){
        return new JobStepBuilder(new StepBuilder("childJob-02"))
                .job(childJobTwo)
                .launcher(jobLauncher)
                .repository(jobRepository)
                .transactionManager(manager)
                .build();
    }
}
