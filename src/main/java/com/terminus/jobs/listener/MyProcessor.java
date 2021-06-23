package com.terminus.jobs.listener;

import org.springframework.batch.item.ItemProcessor;

public class MyProcessor implements ItemProcessor<String,String> {

    @Override
    public String process(String s) throws Exception {
        System.out.println("STARTING PROCESS "+s);
        return s+"_processed!";
    }
}
