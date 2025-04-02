### Quartz Scheduler 기능을 배우기위한 프로젝트
### 설정법
1. applcation.yml에 quartz에서 사용할 data source 설정
2. QuartzConfig 클래스에서 Quartz JobFactory Bean 생성
3. SpringJobFactory 클래스에서 Quartz 작업에 Spring 관련 의존성을 추가
4. 
### Quartz Scheduler 기본 정보
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

## 스크립트 예제

```sql
CREATE TABLE QRTZ_JOB_DETAILS (
    SCHED_NAME VARCHAR(120) NOT NULL,
    JOB_NAME VARCHAR(200) NOT NULL,
    JOB_GROUP VARCHAR(200) NOT NULL,
    DESCRIPTION VARCHAR(250) NULL,
    JOB_CLASS_NAME VARCHAR(250) NOT NULL,
    IS_DURABLE BOOLEAN NOT NULL,
    IS_NONCONCURRENT BOOLEAN NOT NULL,
    IS_UPDATE_DATA BOOLEAN NOT NULL,
    REQUESTS_RECOVERY BOOLEAN NOT NULL,
    JOB_DATA BLOB NULL,
    PRIMARY KEY (SCHED_NAME, JOB_NAME, JOB_GROUP)
);
```

## 스케줄링 코드

```sql
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail sampleJobDetail() {
        return JobBuilder.newJob(SampleJob.class)
                .withIdentity("sampleJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger sampleJobTrigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(10) // 10초마다 실행
                .repeatForever();

        return TriggerBuilder.newTrigger()
                .forJob(sampleJobDetail())
                .withIdentity("sampleTrigger")
                .withSchedule(scheduleBuilder)
                .build();
    }
}
```