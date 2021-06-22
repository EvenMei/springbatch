package com.terminus.pojo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
//@Slf4j
public class Student {
    private Integer id;
    private String name;
    private Integer age;
    private String address;
}
