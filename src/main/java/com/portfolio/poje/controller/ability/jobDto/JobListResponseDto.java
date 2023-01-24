package com.portfolio.poje.controller.ability.jobDto;

import com.portfolio.poje.domain.ability.Job;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class JobListResponseDto {

    private List<JobInfoResponseDto> jobInfoResponseDtoList;


    public JobListResponseDto(List<Job> jobs){
        this.jobInfoResponseDtoList = jobs.stream()
                .map(job -> new JobInfoResponseDto(job.getName()))
                .collect(Collectors.toList());
    }
}
