package org.example.config.listener;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

@Slf4j
public class CustomJobListener implements JobListener {
    @Override
    public String getName() {
        return "CustomJobListener";
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        log.info("[{}] START JOB",context.getJobDetail().getKey());
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        log.info("[{}] DENIED JOB",context.getJobDetail().getKey());
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException e) {
        log.info("[{}] JOB END ",context.getJobDetail().getKey());
        if (e != null){
            log.info("[{}] EXIST EXECPTION JOB",context.getJobDetail().getKey());
        }
    }
}
