package com.portfolio.poje.controller.project.projectDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProjectCreateRequestDto {

    private String name;

    private String duration;

    private String description;

    private String belong;

    private String link;

}
