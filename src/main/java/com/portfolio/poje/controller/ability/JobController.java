package com.portfolio.poje.controller.ability;

import com.portfolio.poje.common.BasicResponse;
import com.portfolio.poje.controller.ability.jobDto.JobListResponse;
import com.portfolio.poje.service.ability.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class JobController {

    private final JobService jobService;


    /**
     * 새로운 직무 등록 (관리자 권한)
     * @param jobMap
     * @return
     */
    @PostMapping("/admin/job")
    public ResponseEntity<BasicResponse> enroll(@RequestBody Map<String, String> jobMap){
        jobService.createJob(jobMap.get("name"));

        return ResponseEntity.ok(new BasicResponse(HttpStatus.CREATED.value(), "직무가 추가되었습니다."));
    }


    /**
     * 직무 목록 반환
     * @return : JobListResponse
     */
    @GetMapping("/member/job")
    public ResponseEntity<BasicResponse> getJobList(){
        JobListResponse jobListResponse = jobService.getJobList();

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "직무 목록 반환", jobListResponse));
    }


    /**
     * 직무 정보 수정 (관리자 권한)
     * @param jobId
     * @param jobMap
     * @return : JobListResponse
     */
    @PutMapping("/admin/job/{job_id}")
    public ResponseEntity<BasicResponse> updateJobInfo(@PathVariable(value = "job_id") Long jobId,
                                                       @RequestBody Map<String, String> jobMap){
        JobListResponse jobListResponse = jobService.updateJobInfo(jobId, jobMap.get("name"));

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "직무 정보가 수정되었습니다.", jobListResponse));
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
