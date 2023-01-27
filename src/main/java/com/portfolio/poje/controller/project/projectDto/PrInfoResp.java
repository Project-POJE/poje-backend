package com.portfolio.poje.controller.project.projectDto;

import com.portfolio.poje.controller.project.projectAwardDto.PrAwardInfoResp;
import com.portfolio.poje.controller.project.projectImgDto.PrImgInfoResp;
import com.portfolio.poje.controller.project.projectSkillDto.PrSkillInfoResp;
import com.portfolio.poje.domain.project.Project;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class PrInfoResp {

    private String name;

    private String duration;

    private String description;

    private String belong;

    private String link;

    PrAwardInfoResp prAwardInfoResp;

    List<PrSkillInfoResp> prSkillInfoRespList;

    List<PrImgInfoResp> prImgInfoRespList;


    @Builder
    private PrInfoResp(Project project){
        this.name = project.getName();
        this.duration = project.getDuration();
        this.description = project.getDescription();
        this.belong = project.getBelong();
        this.link = project.getLink();

        this.prAwardInfoResp = toProjectAwardDto(project);
        this.prSkillInfoRespList = toProjectSkillDto(project);
        this.prImgInfoRespList = toProjectImgDto(project);
    }


    private PrAwardInfoResp toProjectAwardDto(Project project){
        return PrAwardInfoResp.builder()
                .supervision(project.getProjectAward().getSupervision())
                .grade(project.getProjectAward().getGrade())
                .description(project.getProjectAward().getDescription())
                .build();
    }


    private List<PrSkillInfoResp> toProjectSkillDto(Project project){
        return project.getProjectSkills().stream()
                .map(skill -> new PrSkillInfoResp(skill.getType(), skill.getSkill()))
                .collect(Collectors.toList());
    }


    private List<PrImgInfoResp> toProjectImgDto(Project project){
        return project.getProjectImgs().stream()
                .map(img -> PrImgInfoResp.builder()
                        .originalName(img.getOriginalName())
                        .filePath(img.getFilePath())
                        .fileSize(img.getFileSize())
                        .build())
                .collect(Collectors.toList());
    }





}
