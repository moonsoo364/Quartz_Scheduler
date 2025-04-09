package org.example.do_job.vo;

import lombok.Data;

@Data
public class JobRequestVO {

    private String jobName;
    private String jobGroup;
    private String cronExpression = "0 * * ? * * *"; // Default cron expression for every minute
    private String jobDescription;
    private Long jobHourPerWeek = 0L; // Default job hour per week

    private String triggerName;
    private String triggerGroup;
    private String triggerDescription;

}
