package org.example.do_job.service;

import org.example.do_job.vo.JobRequestVO;

public interface JobService {
    void startJob(JobRequestVO param);

    void stopJob(JobRequestVO param);

    void updateJob(JobRequestVO param);

    void deleteJob(JobRequestVO param);
}
