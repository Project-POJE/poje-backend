package com.portfolio.poje.service.ability;

import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
import com.portfolio.poje.controller.ability.jobDto.JobCreateReq;
import com.portfolio.poje.controller.ability.jobDto.JobListResp;
import com.portfolio.poje.controller.ability.jobDto.JobUpdateReq;
import com.portfolio.poje.domain.ability.Job;
import com.portfolio.poje.repository.ability.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class JobService {

    private final JobRepository jobRepository;


    /**
     * 직무 등록
     * @param jobCreateReq
     */
    @Transactional
    public void createJob(JobCreateReq jobCreateReq){
        Job job = Job.enrollJob()
                .name(jobCreateReq.getName())
                .build();

        jobRepository.save(job);
    }


    /**
     * 직무 목록 반환
     * @return : JobListResp
     */
    @Transactional(readOnly = true)
    public JobListResp getJobList(){
        List<Job> jobs = jobRepository.findAll();
        List<String> jobNameList = new ArrayList<>();

        jobNameList.add("전체");
        for (Job job : jobs){
            jobNameList.add(job.getName());
        }

        return new JobListResp(jobNameList);
    }


    /**
     * 직무 정보 수정 후 목록 반환
     * @param jobId
     * @param jobUpdateReq
     * @return : JobListResp
     */
    public JobListResp updateJobInfo(Long jobId, JobUpdateReq jobUpdateReq){
        Job job = jobRepository.findById(jobId).orElseThrow(
                () -> new PojeException(ErrorCode.JOB_NOT_FOUND)
        );

        job.updateJob(jobUpdateReq.getName());
        jobRepository.save(job);

        return getJobList();
    }


    /**
     * 직무 정보 삭제
     * @param jobId
     */
    @Transactional
    public void deleteJobInfo(Long jobId){
        Job job = jobRepository.findById(jobId).orElseThrow(
                () -> new PojeException(ErrorCode.JOB_NOT_FOUND)
        );

        jobRepository.delete(job);
    }


}
