package com.terminus.jobs.itemreaders.xmlreaders;

import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class XmlWriter<Student> implements ItemWriter<Student>{
    public void write(List <? extends Student> list) throws Exception {
        System.out.println("start write ... ");
        for(Student student : list){
            System.out.println(" student ---->  " + student);
        }
    }
}
