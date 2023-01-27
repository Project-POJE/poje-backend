package com.portfolio.poje.controller.ability.jobDto;

import com.portfolio.poje.domain.ability.Job;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class JobListResp {

    private List<JobInfoResp> jobInfoRespList;


    public JobListResp(List<Job> jobs){
        this.jobInfoRespList = jobs.stream()
                .map(job -> new JobInfoResp(job.getId(), job.getName()))
                .collect(Collectors.toList());
    }
}
