package com.terminus.jobs.step.itemreaders;

import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class Writer implements ItemWriter<String> {
    @Override
    public void write(List<? extends String> list) throws Exception {
        for(String val : list){
            System.out.println("【writer】 ----> "+ val);
        }
    }
}
