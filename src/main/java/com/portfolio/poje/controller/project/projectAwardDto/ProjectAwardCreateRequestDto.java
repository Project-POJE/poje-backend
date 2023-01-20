package com.portfolio.poje.controller.project.projectAwardDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProjectAwardCreateRequestDto {

    private String name;

    private String supervision;

    private String grade;

    private String description;


    @Builder
    private ProjectAwardCreateRequestDto(String name, String supervision, String grade, String description){
        this.name = name;
        this.supervision = supervision;
        this.grade = grade;
        this.description = description;
    }
}
