package com.config;

import com.chunk.ExternalSourceData;
import com.chunk.TmUserProcessorConverter;
import com.chunk.UserBatchDataReader;
import com.entity.TmUser;
import com.entity.User;
import com.listener.UserBatchChunkListener;
import com.listener.UserBatchJobListener;
import com.listener.UserBatchStepListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.builder.FaultTolerantStepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
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
import java.util.List;
import java.util.UUID;

@Configuration
public class SpringBatchDataConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private UserBatchDataReader userBatchDataReader;

    @Bean
    public Job userBatchJob(){
        String jobName = UUID.randomUUID().toString().replace("-","");
        return jobBuilderFactory.get(jobName)
                .start(userBatchDataStep())
                .build();
    }

    @Bean
    public Step userBatchDataStep() {
        return new FaultTolerantStepBuilder<ExternalSourceData,TmUser>(stepBuilderFactory.get("userBatchDataStep"))
                    .chunk(2)
                    .reader(userBatchDataReader)
                    .processor(userBatchProcessor())
                    .writer(userBatchDataWriter())
                    .build()
                ;
    }

    @Bean
    public ItemWriter userBatchDataWriter() {
        return new ItemWriter() {
            @Override
            public void write(List list) throws Exception {
                list.forEach(e -> System.out.println("itemWriter:" + e));
            }
        };
    }

    @Bean
    public ItemProcessor<ExternalSourceData, TmUser> userBatchProcessor() {
        return new TmUserProcessorConverter();
    }
}
