package com.config;

import com.entity.User;
import com.listener.UserBatchChunkListener;
import com.listener.UserBatchJobListener;
import com.listener.UserBatchStepListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.orm.JpaNativeQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.UUID;

//@Configuration
public class SpringBatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private UserBatchJobListener userBatchJobListener;
    @Autowired
    private UserBatchStepListener userBatchStepListener;
    @Autowired
    private UserBatchChunkListener userBatchChunkListener;
    //注入实例化Factory 访问数据
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    @Autowired
    private DataSource dataSource;


    @Bean
    public Job userBatchJob(){
        String jobName = UUID.randomUUID().toString().replace("-","");
        return jobBuilderFactory.get(jobName)
                .listener(userBatchJobListener)
                .start(userBatchStep())
                .build();
    }

    @Bean
    public Step userBatchStep() {
        return stepBuilderFactory.get("userBatchStep")
                .listener(userBatchStepListener)
                .<User,User>chunk(2)
                //捕捉到异常就重试,重试3次还是异常,JOB就停止并标志失败
                .faultTolerant().retryLimit(1).retry(Exception.class).skipLimit(3).skip(Exception.class)
                .reader(userBatchReader())
                .processor(userBatchProcessor())
                .writer(userBatchWriter())
                .listener(userBatchChunkListener)
                .build();
    }

    @Bean
    public ItemProcessor<User, User> userBatchProcessor() {
        return new ItemProcessor<User, User>() {
            @Override
            public User process(User user) throws Exception {
                //模拟  假装处理数据,这里处理就是打印一下
                System.out.println("itemProcessor :" + user);
                return user;
            }
        };
    }

    @Bean
    public JdbcBatchItemWriter<User> userBatchWriter() {
        JdbcBatchItemWriter<User> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(dataSource);
        String sql = "insert into customer(username,age) values(:username,:age)";
        writer.setSql(sql);
        BeanPropertyItemSqlParameterSourceProvider<User> sourceProvider = new BeanPropertyItemSqlParameterSourceProvider<>();
        writer.setItemSqlParameterSourceProvider(sourceProvider);
        return writer;
    }

    @Bean
    public JpaPagingItemReader<User> userBatchReader() {
        JpaPagingItemReader<User> reader = new JpaPagingItemReader<>();
        String sql = "select * from user";
        try{
            JpaNativeQueryProvider<User> queryProvider = new JpaNativeQueryProvider<>();
            queryProvider.setSqlQuery(sql);
            queryProvider.setEntityClass(User.class);
            queryProvider.afterPropertiesSet();

            reader.setEntityManagerFactory(entityManagerFactory);
            reader.setPageSize(2);
            reader.setQueryProvider(queryProvider);
            reader.afterPropertiesSet();
            //所有ItemReader和ItemWriter实现都会在ExecutionContext提交之前将其当前状态存储在其中
            // 如果不希望这样做,可以设置setSaveState(false)
            reader.setSaveState(true);
        }catch (Exception e){
            e.printStackTrace();
        }
        return reader;
    }


    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(50);
        threadPoolTaskExecutor.setMaxPoolSize(200);
        threadPoolTaskExecutor.setQueueCapacity(1000);
        threadPoolTaskExecutor.setThreadNamePrefix("data-batch-job");
        return threadPoolTaskExecutor;
    }
}
