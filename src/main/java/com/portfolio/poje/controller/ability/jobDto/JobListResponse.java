package com.portfolio.poje.controller.ability.jobDto;

import com.portfolio.poje.domain.ability.Job;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class JobListResponse {

    private List<JobInfoResponse> jobInfoResponseList;


    public JobListResponse(List<Job> jobs){
        this.jobInfoResponseList = jobs.stream()
                .map(job -> new JobInfoResponse(job.getId(), job.getName()))
                .collect(Collectors.toList());
    }
}
