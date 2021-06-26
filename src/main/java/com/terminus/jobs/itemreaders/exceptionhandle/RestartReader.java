package com.terminus.jobs.itemreaders.exceptionhandle;

import com.terminus.pojo.Student;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.Resource;

import java.util.Objects;

//@Configuration
public class RestartReader implements ItemStreamReader<Student> {


    private Long curLine = 0L;
    private boolean restart = false;
    private ExecutionContext executionContext;

    private FlatFileItemReader<Student> fileItemReader = new FlatFileItemReader<>();
    public RestartReader(Resource resource) {
        fileItemReader.setResource(resource );
        fileItemReader.setLineMapper(getLineMapper());
    }

    /**
     * 第二步 调用read方法进行读取
     *
     * @return
     * @throws Exception
     * @throws UnexpectedInputException
     * @throws ParseException
     * @throws NonTransientResourceException
     */
    @Override
    public Student read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        Student student = null;
        if(restart){
            fileItemReader.setLinesToSkip(curLine.intValue());  // 会出发update操作
            restart = false;
            System.out.println("restart to reading from page : "+ (curLine +1));
        }
        fileItemReader.open(executionContext); //读之前需要先打开文件
        student  = fileItemReader.read();
        if(Objects.nonNull(student)){
            curLine++;
        }
        if(null != student && "stopped".equals( student.getName())){
            throw new RuntimeException("ERROR RAD STOPPED  NAME !");
        }
        return student;
    }

    /**
     * 第一步 先调用 open方法
     *
     * @param executionContext
     * @throws ItemStreamException
     */
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        this.executionContext  = executionContext;
        if(executionContext.containsKey("curLine")){
            restart = true;
            curLine = executionContext.getLong( "curLine");
        }else{
            executionContext.putLong("curLine", 0L);
            System.out.println("start to read from page : "+curLine);
        }
    }

    /**
     * 第三步 读完一批后开始更新
     * @param executionContext
     * @throws ItemStreamException
     */
    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        executionContext.putLong("curLine", curLine);
        System.out.println("now reading to ....... " + curLine+" Line");
    }

    @Override
    public void close() throws ItemStreamException {

    }

    public LineMapper<Student> getLineMapper(){
        DefaultLineMapper<Student> mapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(new String[]{"id","age","name","address"});
        mapper.setLineTokenizer(tokenizer);
        mapper.setFieldSetMapper(getFieldSetMapper());
        return mapper;
    }
    public FieldSetMapper<Student> getFieldSetMapper(){
        return fieldSet -> {
           Student student  = new Student();
           student.setId(fieldSet.readInt("id"));
           student.setAge(fieldSet.readInt("age"));
           student.setName(fieldSet.readString("name"));
           student.setAddress(fieldSet.readString("address"));
           return student;
        };
    }
}
