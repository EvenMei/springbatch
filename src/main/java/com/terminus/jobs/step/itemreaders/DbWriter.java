package com.terminus.jobs.step.itemreaders;

import com.terminus.pojo.Student;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class DbWriter implements ItemWriter<Student> {
    @Override
    public void write(List<? extends Student> list) throws Exception {
        System.out.println("list size is :     " + list.size());
        for (Student student : list){
            System.out.println("【writer】 ---->  " + student);
        }
        /*BufferedWriter fw = new BufferedWriter(new FileWriter("/Users/meiyukai/Desktop/test.txt",true));
        for(Student student: list){
            StringBuffer sb = new StringBuffer();
            sb.append(student.getId()).append(",")
                    .append(student.getAge()).append(",")
                    .append(student.getName()).append(",")
                    .append(student.getAddress());

            fw.write(sb.toString());
            fw.newLine();
        }
        fw.close();*/
    }
}
