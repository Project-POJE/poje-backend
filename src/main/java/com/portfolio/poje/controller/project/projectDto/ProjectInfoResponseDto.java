package com.portfolio.poje.controller.project.projectDto;

import com.portfolio.poje.controller.project.projectAwardDto.ProjectAwardInfoResponseDto;
import com.portfolio.poje.controller.project.projectImgDto.ProjectImgInfoResponseDto;
import com.portfolio.poje.controller.project.projectSkillDto.ProjectSkillInfoResponseDto;
import com.portfolio.poje.domain.project.Project;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class ProjectInfoResponseDto {

    private String name;

    private String duration;

    private String description;

    private String belong;

    private String link;

    ProjectAwardInfoResponseDto projectAwardInfoResponseDto;

    List<ProjectSkillInfoResponseDto> projectSkillInfoResponseDtoList;

    List<ProjectImgInfoResponseDto> projectImgInfoResponseDtoList;


    @Builder
    private ProjectInfoResponseDto(Project project){
        this.name = project.getName();
        this.duration = project.getDuration();
        this.description = project.getDescription();
        this.belong = project.getBelong();
        this.link = project.getLink();

        this.projectAwardInfoResponseDto = toProjectAwardDto(project);
        this.projectSkillInfoResponseDtoList = toProjectSkillDto(project);
        this.projectImgInfoResponseDtoList = toProjectImgDto(project);
    }


    private ProjectAwardInfoResponseDto toProjectAwardDto(Project project){
        return ProjectAwardInfoResponseDto.builder()
                .supervision(project.getProjectAward().getSupervision())
                .grade(project.getProjectAward().getGrade())
                .description(project.getProjectAward().getDescription())
                .build();
    }


    private List<ProjectSkillInfoResponseDto> toProjectSkillDto(Project project){
        return project.getProjectSkills().stream()
                .map(skill -> new ProjectSkillInfoResponseDto(skill.getSkill()))
                .collect(Collectors.toList());
    }


    private List<ProjectImgInfoResponseDto> toProjectImgDto(Project project){
        return project.getProjectImgs().stream()
                .map(img -> ProjectImgInfoResponseDto.builder()
                        .originalName(img.getOriginalName())
                        .filePath(img.getFilePath())
                        .fileSize(img.getFileSize())
                        .build())
                .collect(Collectors.toList());
    }





}
