package com.terminus.jobs.jobparameters;

import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class MyWriter implements ItemWriter<String> {
    @Override
    public void write(List<? extends String> list) throws Exception {
        for(String num : list){
            System.out.println("num ="+num);
        }
    }
}
