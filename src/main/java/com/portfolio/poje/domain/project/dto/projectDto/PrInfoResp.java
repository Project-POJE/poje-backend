package com.portfolio.poje.domain.project.dto.projectDto;

import com.portfolio.poje.domain.project.entity.Project;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PrInfoResp {

    private Long projectId;

    private String name;

    private String duration;

    private String description;

    private String belong;

    private String link;


    @Builder
    private PrInfoResp(Project project){
        this.projectId = project.getId();
        this.name = project.getName();
        this.duration = project.getDuration();
        this.description = project.getDescription();
        this.belong = project.getBelong();
        this.link = project.getLink();
    }

}
