package com.terminus.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 需要在yml文件中设置 spring.batch.job.enable=false
 */
@Controller
public class JobLauncherController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @RequestMapping(value = "/start")
    public String startLauncher(){
        return "joblauncher";
    }

    @GetMapping("/startJob")
    public String  start() throws Exception {
        System.out.println(" intoStartMethod .... " );
        JobParameters jobParameter = new JobParametersBuilder()
                .addString("msg","job1")
                .toJobParameters();

        jobLauncher.run(job, jobParameter);

        return "redirect:/start";
    }

}
