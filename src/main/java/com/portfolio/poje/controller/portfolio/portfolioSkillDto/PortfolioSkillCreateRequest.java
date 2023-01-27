package com.portfolio.poje.controller.portfolio.portfolioSkillDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PortfolioSkillCreateRequest {

    private Long portfolioId;

    private List<PortfolioSkillInfoRequest> skills;

}
