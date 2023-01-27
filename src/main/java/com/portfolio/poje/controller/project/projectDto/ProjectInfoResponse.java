package com.portfolio.poje.controller.project.projectDto;

import com.portfolio.poje.controller.project.projectAwardDto.ProjectAwardInfoResponse;
import com.portfolio.poje.controller.project.projectImgDto.ProjectImgInfoResponse;
import com.portfolio.poje.controller.project.projectSkillDto.ProjectSkillInfoResponse;
import com.portfolio.poje.domain.project.Project;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class ProjectInfoResponse {

    private String name;

    private String duration;

    private String description;

    private String belong;

    private String link;

    ProjectAwardInfoResponse projectAwardInfoResponse;

    List<ProjectSkillInfoResponse> projectSkillInfoResponseList;

    List<ProjectImgInfoResponse> projectImgInfoResponseList;


    @Builder
    private ProjectInfoResponse(Project project){
        this.name = project.getName();
        this.duration = project.getDuration();
        this.description = project.getDescription();
        this.belong = project.getBelong();
        this.link = project.getLink();

        this.projectAwardInfoResponse = toProjectAwardDto(project);
        this.projectSkillInfoResponseList = toProjectSkillDto(project);
        this.projectImgInfoResponseList = toProjectImgDto(project);
    }


    private ProjectAwardInfoResponse toProjectAwardDto(Project project){
        return ProjectAwardInfoResponse.builder()
                .supervision(project.getProjectAward().getSupervision())
                .grade(project.getProjectAward().getGrade())
                .description(project.getProjectAward().getDescription())
                .build();
    }


    private List<ProjectSkillInfoResponse> toProjectSkillDto(Project project){
        return project.getProjectSkills().stream()
                .map(skill -> new ProjectSkillInfoResponse(skill.getType(), skill.getSkill()))
                .collect(Collectors.toList());
    }


    private List<ProjectImgInfoResponse> toProjectImgDto(Project project){
        return project.getProjectImgs().stream()
                .map(img -> ProjectImgInfoResponse.builder()
                        .originalName(img.getOriginalName())
                        .filePath(img.getFilePath())
                        .fileSize(img.getFileSize())
                        .build())
                .collect(Collectors.toList());
    }





}
