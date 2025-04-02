package org.example.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobKey key = context.getJobDetail().getKey();

        JobDataMap dataMap = context.getJobDetail().getJobDataMap();

        String jobSays = dataMap.getString("jobSays");
        float myFloatValue = dataMap.getFloat("myFloatValue");

        log.info("Instance " + key + " jobSays: " + jobSays + ", and myFloatValue: " + myFloatValue);
    }
}

