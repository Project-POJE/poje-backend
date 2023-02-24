package com.portfolio.poje.domain.project.dto;

import com.portfolio.poje.domain.project.entity.Project;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

public class PrDto {

    /**
     * 프로젝트 정보 수정 요청 Dto
     */
    @Getter
    @NoArgsConstructor
    public static class PrInfoReq {

        private String name;

        private String duration;

        private String description;

        private String belong;

        private String link;
    }


    /**
     * 프로젝트 & 수상 정보 & 기술 정보 수정 요청 Dto
     */
    @Getter
    @NoArgsConstructor
    public static class PrUpdateReq {

        private PrInfoReq prInfo;

        private PrAwardDto.PrAwardUpdateReq prAwardInfo;

        private List<PrSkillDto.PrSkillListReq> skillSet;
    }


    /**
     * 프로젝트 정보 응답 Dto
     */
    @Getter
    public static class PrInfoResp {

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


    /**
     * 프로젝트 관련 정보 응답 Dto
     */
    @Getter
    public static class PrAllInfoResp {

        private PrInfoResp prInfo;

        private PrAwardDto.PrAwardInfoResp prAwardInfo;

        private List<PrSkillDto.PrSkillListResp> prSkillList;

        private List<String> prImgList;

        @Builder
        private PrAllInfoResp(Project project, List<PrSkillDto.PrSkillListResp> prSkillList){
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

        private PrAwardDto.PrAwardInfoResp toProjectAwardDto(Project project){
            if (project.getProjectAward() == null) {
                return PrAwardDto.PrAwardInfoResp.builder()
                        .supervision("주관을 입력해주세요.")
                        .grade("순위를 입력해주세요. (e.g.3등 or 동상)")
                        .description("설명을 입력해주세요.")
                        .build();
            }
            return PrAwardDto.PrAwardInfoResp.builder()
                    .supervision(project.getProjectAward().getSupervision())
                    .grade(project.getProjectAward().getGrade())
                    .description(project.getProjectAward().getDescription())
                    .build();
        }

        private List<String> toProjectImgDto(Project project){
            return project.getProjectImgs().stream()
                    .map(img -> img.getUrl())
                    .collect(Collectors.toList());
        }
    }


}
