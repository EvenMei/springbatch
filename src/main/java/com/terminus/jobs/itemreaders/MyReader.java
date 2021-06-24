package com.terminus.jobs.itemreaders;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.Iterator;
import java.util.List;

public class MyReader implements ItemReader<String> {
    public List<String> data;
    public Iterator<String> iterator;
    private  int count = 0;

    public MyReader(List<String> data) {
        this.data = data;
        this.iterator = data.iterator();
    }

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if(iterator.hasNext()){
            String res = iterator.next();
            System.out.println("【Reader】 ----> "+res);
            return res;
        }
        return null; //读到null 就停止
        /*if (count < data.size()) {
            return data.get(count++);
        }
            return null;*/
    }
}
