package com.portfolio.poje.controller.portfolio.portfolioDto;

import com.portfolio.poje.domain.portfolio.Portfolio;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PfAndMemberListResp {

    private List<PfAndMemberResp> pfAndMemberResp;


    @Builder
    private PfAndMemberListResp(List<Portfolio> portfolioList){
        this.pfAndMemberResp = portfolioList.stream()
                .map(portfolio -> PfAndMemberResp.builder()
                        .portfolio(portfolio)
                        .build())
                .collect(Collectors.toList());
    }

}
