package com.portfolio.poje.service.ability;

import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
import com.portfolio.poje.controller.ability.jobDto.JobListResponseDto;
import com.portfolio.poje.domain.ability.Job;
import com.portfolio.poje.repository.ability.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class JobService {

    private final JobRepository jobRepository;


    /**
     * 직무 등록
     * @param name
     */
    @Transactional
    public void createJob(String name){
        Job job = Job.enrollJob()
                .name(name)
                .build();

        jobRepository.save(job);
    }


    /**
     * 직무 목록 반환
     * @return : JobListResponseDto
     */
    @Transactional(readOnly = true)
    public JobListResponseDto getJobList(){
        List<Job> jobs = jobRepository.findAll();

        return new JobListResponseDto(jobs);
    }


    /**
     * 직무 정보 수정 후 목록 반환
     * @param jobId
     * @param name
     * @return : JobListResponseDto
     */
    public JobListResponseDto updateJobInfo(Long jobId, String name){
        Job job = jobRepository.findById(jobId).orElseThrow(
                () -> new PojeException(ErrorCode.JOB_NOT_FOUND)
        );

        job.updateJob(name);
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
