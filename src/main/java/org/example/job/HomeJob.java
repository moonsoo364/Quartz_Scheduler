package org.example.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HomeJob implements Job {


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobKey key = context.getJobDetail().getKey();
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        String jobName = dataMap.getString("jobName");
        float jobHourPerWeek = dataMap.getFloat("jobHourPerWeek");

        log.info("[{}] jobName : {} , jobHourPerWeek : {}",key,jobName,jobHourPerWeek);

    }
}
