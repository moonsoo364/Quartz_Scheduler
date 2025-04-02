### Quartz Scheduler 기능을 배우기위한 프로젝트
### 설정법
1. applcation.yml에 quartz에서 사용할 data source 설정
2. QuartzConfig 클래스에서 Quartz JobFactory Bean 생성
3. SpringJobFactory 클래스에서 Quartz 작업에 Spring 관련 의존성을 추가
# Quartz Scheduler 기본 정보

## DB 테이블 정보

| 테이블 이름 | 설명 |
| --- | --- |
| QRTZ_JOB_DETAILS | 잡(Job)정보를 저장 |
| QRTZ_TRIGGERS | 트리거(Trigger) 정보를 저장 |
| QRTZ_CRON_TRIGGERS | 크론(Cron) 기반의 트리거 정보를 저장 |
| QRTZ_SIMPLE_TRIGGERS | 간단한(Simple) 트리거 정보를 저장 |
| QRTZ_BLOB_TRIGGERS | Blob 데이터로 저장된 트리거 정보 |
| QRTZ_CALENDARS | 일정 정보 저장 |
| QRTZ_FIRED_TRIGGERS | 실행된 트리거 정보 저장 |
| QRTZ_PAUSED_TRIGGERS_GRPS | 트리거 그룹정보 |
| QRTZ_SCHEDULER_STATE | 스케줄러 상태 정보 |
| QRTZ_LOCKS | 동시 실행 방지를 위한 락 정 |

## 라이브러리 버전

```xml
      <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-quartz</artifactId>
        <version>2.7.9</version>
      </dependency>
```

## 설정

```yaml
spring:
  quartz:
    job-store-type: jdbc
    properties:
      org.quartz.scheduler.instanceName: myScheduler
      org.quartz.scheduler.instanceId: AUTO
      org.quartz.jobStore.class: org.quartz.impl.jdbcjobstore.JobStoreTX
      org.quartz.jobStore.driverDelegateClass: org.quartz.impl.jdbcjobstore.StdJDBCDelegate
      org.quartz.jobStore.tablePrefix: QRTZ_
      org.quartz.threadPool.threadCount: 10
```

## 스케줄링 코드

MyJob : Job을 구현한 MyJob 클래스에서는 스케줄링을 실행할 비즈니스 로직이 담기는 클래스이다.

JobDetail : 스케줄링에서 사용할 Job 속성을 정의한다. 여기 정의한 Job 정보는 데이터베이스 테이블에서 조회할 수 있다.`usingJobData()`를 이용하여 Job에서 사용할 데이터를 정의할 수 있다.

Trigger : Job의 스캐줄링 주기를 설정할 수 있는 클래스이다. 시간 혹은 Cron을 지정해서 저장할 수 있다.

Scheduler : 스케줄링을 실행하는 클래스이다. JobDetail과 Trigger를 Scheduler에 등록하여 스케줄을 실행한다.
```

@Slf4j
@Component
public class MyJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobKey key = context.getJobDetail().getKey();
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        String jobName = dataMap.getString("jobName");
        float jobHourPerWeek = dataMap.getFloat("jobHourPerWeek");

        log.info("[{}] jobName : {} , jobHourPerWeek : {}",key,jobName,jobHourPerWeek);
    }
}

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Configuration
public class MyJobScheduler extends JobScheduler {

    public MyJobScheduler(Scheduler scheduler) {
        super(scheduler);
    }

    @Bean(name="myJobDetail")
    public JobDetail getJobDetail() {
        return JobBuilder
                .newJob(MyJob.class)
                .withIdentity("myJob","group1")
                .usingJobData("jobName", "Spring App Management") //JobDataMap형식으로 데이터를 넣음
                .usingJobData("jobHourPerWeek", 40.0f)
                .storeDurably()// JobDetail을 영구적으로 저장하도록 설정하는 메서드, 트리거가 삭제되더라도 JobDetail이 삭제되지 않고 스케줄러에 남아있다
                .build();
    }

    @Bean(name="myJobTrigger")
    public Trigger getJobTrigger(@Qualifier("myJobDetail") JobDetail jobDetail) throws SchedulerException {
        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("myTrigger","group1")
                .startAt(new Date())// 즉시 시작
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInSeconds(10) // 10초 마다 실행
                                .repeatForever())
                .build();
        addTrigger(jobDetail, trigger);
        return trigger;
    }

   }
}

@Component
@RequiredArgsConstructor
public abstract class JobScheduler {

    protected final Scheduler scheduler;

    public abstract JobDetail getJobDetail();

    public abstract Trigger getJobTrigger(JobDetail myJobDetail) throws SchedulerException;

    public void addTrigger(JobDetail myJobDetail, Trigger myJobTrigger) throws SchedulerException {
        scheduler.scheduleJob(myJobDetail, myJobTrigger);
    }
}
```

## 데이터베이스에 저장된 JOB 정보
application.yml에 설정한 DB에 현재 등록된 Job 목록을 볼 수 있다.
아래는 `QRTZ_JOB_DETAILS` 테이블의 행과 열이다.

| SCHED_NAME           | JOB_NAME | JOB_GROUP | DESCRIPTION | JOB_CLASS_NAME               | IS_DURABLE | IS_NONCONCURRENT | IS_UPDATE_DATA | REQUESTS_RECOVERY | JOB_DATA                                 |
|----------------------|---------|----------|-------------|------------------------------|------------|-----------------|---------------|-----------------|------------------------------------------|
| schedulerFactoryBean | homeJob | group2   | null        | org.example.job.HomeJob      | TRUE       | FALSE           | FALSE         | FALSE           | 756d62657286ac951d0b94e08b02000078704091999a7800 |
| schedulerFactoryBean | myJob   | group1   | null        | org.example.job.MyJob        | TRUE       | FALSE           | FALSE         | FALSE           | (empty)                                  |
## Listener 설명
Jog, Trigger, Scheduler 의 LifeCycle에 맞춰 실행할 비즈니스 로직이 있을 경우 Listener 클래스를 상속받아 처리할 수 있다.
리스너의 실행 순서는 다음과 같다.
1. 애플리케이션이 실행되면 등록된 Job 개수만큼 `SchedulerListener.jobScheduled()`가 실행된다,
2. Job이 실행될 시간이 되면 `TriggerListener.triggerFired()`가 실행된다. 
3. Job이 시작하기 전 `CustomJobListener.jobToBeExecuted()`가 실행된다. 
4. Job이 끝나면 `CustomJobListener.jobWasExecuted()`가 실행된다. 
5. Trigger가 끝나면 `TriggerListener.triggerComplete()`가 실행된다. 
6. 애플리케이션이 종료되면 `SchedulerListener.schedulerShutdown()`가 실행된다.



```declarative
2025-04-02 16:46:06 INFO  o.e.c.l.CustomSchedulerListener - [group2.homeTrigger] SCHEDULE START
2025-04-02 16:46:06 INFO  o.e.c.l.CustomSchedulerListener - [group1.myTrigger] SCHEDULE START
2025-04-02 16:46:06 INFO  o.e.c.listener.CustomTriggerListener - [group2.homeTrigger] TRIGGER START
2025-04-02 16:46:06 INFO  o.e.c.listener.CustomJobListener - [group2.homeJob] START JOB
2025-04-02 16:46:06 INFO  org.example.job.HomeJob - [group2.homeJob] jobName : Dish wash , jobHourPerWeek : 4.55
2025-04-02 16:46:06 INFO  o.e.c.listener.CustomJobListener - [group2.homeJob] END 
2025-04-02 16:46:06 INFO  o.e.c.listener.CustomTriggerListener - [group2.homeTrigger] TRIGGER SUCCESS
2025-04-02 16:46:06 INFO  o.e.c.listener.CustomTriggerListener - [group1.myTrigger] TRIGGER START
2025-04-02 16:46:06 INFO  o.e.c.listener.CustomJobListener - [group1.myJob] START JOB
2025-04-02 16:46:06 INFO  org.example.job.MyJob - [group1.myJob] jobName : Spring App Management , jobHourPerWeek : 40.0
2025-04-02 16:46:06 INFO  o.e.c.listener.CustomJobListener - [group1.myJob] END 
2025-04-02 16:46:06 INFO  o.e.c.listener.CustomTriggerListener - [group1.myTrigger] TRIGGER SUCCESS
```
