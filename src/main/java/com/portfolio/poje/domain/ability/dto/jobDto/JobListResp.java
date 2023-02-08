package com.portfolio.poje.domain.ability.dto.jobDto;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class JobListResp {

    private List<JobInfoResp> jobInfoRespList;


    public JobListResp(List<String> jobNameList){
        this.jobInfoRespList = jobNameList.stream()
                .map(name -> new JobInfoResp(name))
                .collect(Collectors.toList());
    }
}
