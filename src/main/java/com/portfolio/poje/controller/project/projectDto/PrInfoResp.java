package com.portfolio.poje.controller.project.projectDto;

import com.portfolio.poje.domain.project.Project;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PrInfoResp {

    private String name;

    private String duration;

    private String description;

    private String belong;

    private String link;


    @Builder
    private PrInfoResp(Project project){
        this.name = project.getName();
        this.duration = project.getDuration();
        this.description = project.getDescription();
        this.belong = project.getBelong();
        this.link = project.getLink();
    }

}
