package com.portfolio.poje.controller.project.projectAwardDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PrAwardUpdateReq {

    private Long projectId;

    private String supervision;

    private String grade;

    private String description;

}
