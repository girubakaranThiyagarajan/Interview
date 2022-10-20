package com.member.claim.controller;

import com.member.claim.dto.BatchJobRequest;
import com.member.claim.exception.ClaimBatchJobException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/batch")
@Slf4j
public class ClaimBatchJobController {

    private final JobLauncher jobLauncher;
    private final Job job;

    public ClaimBatchJobController(JobLauncher jobLauncher, Job job) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    @PostMapping("/processClaims")
    public void processClaimInBatch(@RequestBody BatchJobRequest batchJobRequest) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", UUID.randomUUID().toString())
                .addLong("JobId", System.currentTimeMillis())
                .addString("inputFilePath",batchJobRequest.getInputfilepath())
                .addString("outPutFilePath",batchJobRequest.getOutputFilePath())
                .addLong("time", System.currentTimeMillis()).toJobParameters();
        try {
            JobExecution execution = jobLauncher.run(job, jobParameters);
            log.debug("STATUS :: " + execution.getStatus());
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
           log.error("Got Error While Running the job ",e);
           throw new ClaimBatchJobException("Got Error While Running the job ");
        }
    }
}
