package com.listener;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * job监听器
 */
@Component
public class UserBatchJobListener implements JobExecutionListener {

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private long startTime;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        startTime = System.currentTimeMillis();
        System.out.println("userBatchJobListener  beforeJob:" + jobExecution.getJobParameters());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("job的执行状态:" + jobExecution.getStatus());
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            threadPoolTaskExecutor.destroy();
        }
        System.out.println("userBatchJobListener执行耗时(ms):" + (System.currentTimeMillis() - startTime));
    }
}
