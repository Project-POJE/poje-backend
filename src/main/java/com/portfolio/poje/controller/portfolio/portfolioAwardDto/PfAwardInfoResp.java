package com.portfolio.poje.controller.portfolio.portfolioAwardDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PfAwardInfoResp {

    private String supervision;

    private String grade;

    private String description;

    @Builder
    private PfAwardInfoResp(String supervision, String grade, String description){
        this.supervision = supervision;
        this.grade = grade;
        this.description = description;
    }
}