package com.portfolio.poje.domain.ability.controller;

import com.portfolio.poje.common.BasicResponse;
import com.portfolio.poje.domain.ability.dto.JobDto;
import com.portfolio.poje.domain.ability.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class JobController {

    private final JobService jobService;


    /**
     * 새로운 직무 등록 (관리자 권한)
     * @param jobCreateReq
     * @return
     */
    @PostMapping("/admin/job")
    public ResponseEntity<BasicResponse> enroll(@RequestBody @Valid JobDto.JobCreateReq jobCreateReq){
        jobService.createJob(jobCreateReq);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.CREATED.value(), "직무가 추가되었습니다."));
    }


    /**
     * 직무 목록 반환
     * @return : JobListResp
     */
    @GetMapping("/member/job")
    public ResponseEntity<BasicResponse> getJobList(){
        JobDto.JobListResp jobListResp = jobService.getJobList();

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "직무 목록 반환", jobListResp));
    }


    /**
     * 직무 정보 수정 (관리자 권한)
     * @param jobId
     * @param jobUpdateReq
     * @return : JobListResp
     */
    @PutMapping("/admin/job/{job_id}")
    public ResponseEntity<BasicResponse> updateJobInfo(@PathVariable(value = "job_id") Long jobId,
                                                       @RequestBody @Valid JobDto.JobUpdateReq jobUpdateReq){
        JobDto.JobListResp jobListResp = jobService.updateJobInfo(jobId, jobUpdateReq);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "직무 정보가 수정되었습니다.", jobListResp));
    }


    /**
     * 직무 정보 삭제 (관리자 권한)
     * @param jobId
     * @return
     */
    @DeleteMapping("/admin/job/{job_id}")
    public ResponseEntity<BasicResponse> deleteJobInfo(@PathVariable(value = "job_id") Long jobId){
        jobService.deleteJobInfo(jobId);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "직무 정보가 삭제되었습니다."));
    }

}
