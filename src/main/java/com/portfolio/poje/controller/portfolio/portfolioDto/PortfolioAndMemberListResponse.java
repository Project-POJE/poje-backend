package com.portfolio.poje.controller.portfolio.portfolioDto;

import com.portfolio.poje.domain.ability.Job;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class PortfolioAndMemberListResponse {

    private List<PortfolioAndMemberResponse> portfolioAndMemberResponses;


    @Builder
    private PortfolioAndMemberListResponse(Job job){
        this.portfolioAndMemberResponses = job.getPortfolioList().stream()
                .map(portfolio -> PortfolioAndMemberResponse.builder()
                        .portfolio(portfolio)
                        .build())
                .collect(Collectors.toList());
    }

}
