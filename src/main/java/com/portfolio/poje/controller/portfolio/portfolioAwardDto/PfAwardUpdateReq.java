package com.portfolio.poje.controller.portfolio.portfolioAwardDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PfAwardUpdateReq {

    private Long portfolioAwardId;

    private String supervision;

    private String grade;

    private String description;

}
