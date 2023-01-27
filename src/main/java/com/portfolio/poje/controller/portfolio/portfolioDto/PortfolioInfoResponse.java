package com.portfolio.poje.controller.portfolio.portfolioDto;

import com.portfolio.poje.controller.ability.licenseDto.LicenseInfoResponse;
import com.portfolio.poje.controller.portfolio.portfolioAwardDto.PortfolioAwardInfoResponse;
import com.portfolio.poje.controller.portfolio.portfolioSkillDto.PortfolioSkillInfoResponse;
import com.portfolio.poje.controller.project.projectDto.ProjectInfoResponse;
import com.portfolio.poje.domain.portfolio.Portfolio;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class PortfolioInfoResponse {

    // Portfolio
    private Long portfolioId;

    private String title;

    private String description;

    private String gitHubLink;

    private String blogLink;

    private String backgroundImg;

    private List<PortfolioAwardInfoResponse> portfolioAwardInfoResponseList;

    private List<PortfolioSkillInfoResponse> portfolioSkillInfoResponseList;

    // Member
    private String nickName;

    private String email;

    private String phoneNum;

    private String birth;

    private String academic;

    private String dept;

    private String profileImg;

    // Job
    private String jobName;

    // License
    private List<LicenseInfoResponse> licenseInfoResponseList;

    // Project
    private List<ProjectInfoResponse> projectInfoResponseList;


    @Builder
    private PortfolioInfoResponse(Portfolio portfolio){
        this.portfolioId = portfolio.getId();
        this.title = portfolio.getTitle();
        this.description = portfolio.getDescription();
        this.gitHubLink = portfolio.getGitHubLink();
        this.blogLink = portfolio.getBlogLink();
        this.backgroundImg = portfolio.getBackgroundImg();
        this.portfolioAwardInfoResponseList = toPortfolioAwardDto(portfolio);
        this.portfolioSkillInfoResponseList = toPortfolioSkillDto(portfolio);
        this.jobName = portfolio.getJob().getName();

        this.nickName = portfolio.getWriter().getNickName();
        this.email = portfolio.getWriter().getEmail();
        this.phoneNum = portfolio.getWriter().getPhoneNum();
        this.birth = portfolio.getWriter().getBirth();
        this.academic = portfolio.getWriter().getAcademic();
        this.dept = portfolio.getWriter().getDept();
        this.profileImg = portfolio.getWriter().getProfileImg();
        this.licenseInfoResponseList = toLicenseDto(portfolio);
        this.projectInfoResponseList = toProjectDto(portfolio);
    }


    public List<PortfolioAwardInfoResponse> toPortfolioAwardDto(Portfolio portfolio){
        return portfolio.getPortfolioAwards().stream()
                .map(award -> PortfolioAwardInfoResponse.builder()
                        .supervision(award.getSupervision())
                        .grade(award.getGrade())
                        .description(award.getDescription())
                        .build())
                .collect(Collectors.toList());
    }


    public List<PortfolioSkillInfoResponse> toPortfolioSkillDto(Portfolio portfolio){
        return portfolio.getPortfolioSkills().stream()
                .map(skill -> new PortfolioSkillInfoResponse(skill.getType(), skill.getSkill()))
                .collect(Collectors.toList());
    }


    public List<LicenseInfoResponse> toLicenseDto(Portfolio portfolio){
        return portfolio.getWriter().getLicenseList().stream()
                .map(license -> new LicenseInfoResponse(license.getName()))
                .collect(Collectors.toList());
    }


    public List<ProjectInfoResponse> toProjectDto(Portfolio portfolio){
        return portfolio.getProjects().stream()
                .map(project -> ProjectInfoResponse.builder()
                        .project(project)
                        .build())
                .collect(Collectors.toList());
    }

}
