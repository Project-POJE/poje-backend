package com.portfolio.poje.domain.portfolio.dto;

import com.portfolio.poje.domain.portfolio.entity.Portfolio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

public class PfDto {

    /**
     * 포트폴리오 수정 요청 Dto
     */
    @Getter
    @NoArgsConstructor
    public static class PfUpdateReq {

        @NotBlank(message = "제목을 입력해주세요.")
        private String title;

        @NotBlank(message = "설명이나 소개를 입력해주세요.")
        private String description;
    }


    /**
     * 포트폴리오 생성 정보 응답 Dto
     */
    @Getter
    @AllArgsConstructor
    public static class PfCreateResp {

        private Long portfolioId;
    }


    /**
     * 포트폴리오 정보 응답 Dto
     */
    @Getter
    public static class PfInfoResp {

        private Long portfolioId;

        private String title;

        private String description;

        private String backgroundImg;

        private String jobName;

        @Builder
        private PfInfoResp(Portfolio portfolio){
            this.portfolioId = portfolio.getId();
            this.title = portfolio.getTitle();
            this.description = portfolio.getDescription();
            this.backgroundImg = portfolio.getBackgroundImg();
            this.jobName = portfolio.getJob().getName();
        }
    }


    /**
     * 포트폴리오 About Me 응답 Dto
     */
    @Getter
    public static class PfAboutMeResp {

        private String nickName;

        private String email;

        private String phoneNum;

        private String gender;

        private String academic;

        private String dept;

        private String birth;

        private String profileImg;

        private String gitHubLink;

        private String blogLink;

        @Builder
        private PfAboutMeResp(Portfolio portfolio){
            this.nickName = portfolio.getWriter().getNickName();
            this.email = portfolio.getWriter().getEmail();
            this.phoneNum = portfolio.getWriter().getPhoneNum();
            this.gender = portfolio.getWriter().getGender();
            this.academic = portfolio.getWriter().getAcademic();
            this.dept = portfolio.getWriter().getDept();
            this.birth = portfolio.getWriter().getBirth();
            this.profileImg = portfolio.getWriter().getProfileImg();
            this.gitHubLink = portfolio.getWriter().getGitHubLink();
            this.blogLink = portfolio.getWriter().getBlogLink();
        }
    }


    /**
     * 포트폴리오 & 사용자 정보 응답 Dto
     */
    @Getter
    public static class PfAndMemberResp {
        // Portfolio
        private Long portfolioId;

        private String title;

        private String description;

        private String backgroundImg;

        // Member
        private String nickName;

        private String profileImg;

        // PortfolioLike
        private int likeCount;

        @Builder
        private PfAndMemberResp(Portfolio portfolio){
            this.portfolioId = portfolio.getId();
            this.title = portfolio.getTitle();
            this.description = portfolio.getDescription();
            this.backgroundImg = portfolio.getBackgroundImg();

            this.nickName = portfolio.getWriter().getNickName();
            this.profileImg = portfolio.getWriter().getProfileImg();

            this.likeCount = portfolio.getLikes().size();
        }
    }


    /**
     * 포트폴리오 & 작성자 정보 목록 응답 Dto
     */
    @Getter
    public static class PfAndMemberListResp {

        private List<PfAndMemberResp> pfAndMemberResp;

        @Builder
        private PfAndMemberListResp(List<Portfolio> portfolioList){
            this.pfAndMemberResp = portfolioList.stream()
                    .map(portfolio -> PfAndMemberResp.builder()
                            .portfolio(portfolio)
                            .build())
                    .collect(Collectors.toList());
        }
    }


}
