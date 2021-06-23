package com.terminus.jobs.jobparameters;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.Random;

public class MyReader implements ItemReader<String> {
    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return  "read ===> "+String.valueOf(new Random().nextInt(100));
    }

}
