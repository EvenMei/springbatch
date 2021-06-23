package com.terminus.jobs.jobparameters;

import org.springframework.batch.item.ItemProcessor;

public class MyProcessor implements ItemProcessor<String,String> {
    @Override
    public String process(String s) throws Exception {
        System.out.println("PROCESSING ===> "+s);
        return s+"processed!";
    }
}
