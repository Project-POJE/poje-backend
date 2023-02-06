package com.portfolio.poje.controller.project.projectDto;

import com.portfolio.poje.controller.project.projectAwardDto.PrAwardInfoResp;
import com.portfolio.poje.controller.project.projectImgDto.PrImgInfoResp;
import com.portfolio.poje.controller.project.projectSkillDto.PrSkillListResp;
import com.portfolio.poje.domain.project.Project;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PrAllInfoResp {

    private PrInfoResp prInfo;

    private PrAwardInfoResp prAwardInfo;

    private List<PrSkillListResp> prSkillList;

    private List<PrImgInfoResp> prImgList;


    @Builder
    private PrAllInfoResp(Project project, List<PrSkillListResp> prSkillList){
        this.prInfo = toProjectInfoDto(project);
        this.prAwardInfo = toProjectAwardDto(project);
        this.prSkillList = prSkillList;
        this.prImgList = toProjectImgDto(project);
    }


    private PrInfoResp toProjectInfoDto(Project project){
        return PrInfoResp.builder()
                .project(project)
                .build();
    }

    private PrAwardInfoResp toProjectAwardDto(Project project){
        if (project.getProjectAward() == null) {
            return PrAwardInfoResp.builder()
                    .supervision("주관을 입력해주세요.")
                    .grade("순위를 입력해주세요. (e.g.3등 or 동상)")
                    .description("설명을 입력해주세요.")
                    .build();
        }
        return PrAwardInfoResp.builder()
                .supervision(project.getProjectAward().getSupervision())
                .grade(project.getProjectAward().getGrade())
                .description(project.getProjectAward().getDescription())
                .build();
    }

    private List<PrImgInfoResp> toProjectImgDto(Project project){
        return project.getProjectImgs().stream()
                .map(img -> new PrImgInfoResp(img.getFilePath()))
                .collect(Collectors.toList());
    }





}
