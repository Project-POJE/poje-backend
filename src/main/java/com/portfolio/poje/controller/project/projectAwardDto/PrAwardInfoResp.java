package com.portfolio.poje.controller.project.projectAwardDto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PrAwardInfoResp {

    private String supervision;

    private String grade;

    private String description;


    @Builder
    private PrAwardInfoResp(String supervision, String grade, String description){
        this.supervision = supervision;
        this.grade = grade;
        this.description = description;
    }

}
