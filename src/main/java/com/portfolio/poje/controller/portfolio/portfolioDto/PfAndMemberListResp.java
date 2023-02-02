package com.portfolio.poje.controller.portfolio.portfolioDto;

import com.portfolio.poje.domain.ability.Job;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PfAndMemberListResp {

    private List<PfAndMemberResp> pfAndMemberResp;


    @Builder
    private PfAndMemberListResp(Job job){
        this.pfAndMemberResp = job.getPortfolioList().stream()
                .map(portfolio -> PfAndMemberResp.builder()
                        .portfolio(portfolio)
                        .build())
                .collect(Collectors.toList());
    }

}
