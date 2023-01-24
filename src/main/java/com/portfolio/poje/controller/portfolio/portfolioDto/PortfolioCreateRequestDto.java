package com.portfolio.poje.controller.portfolio.portfolioDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PortfolioCreateRequestDto {

    private Long jobId;

    private String title;

    private String description;

    private String gitHubLink;

    private String blogLink;

}
