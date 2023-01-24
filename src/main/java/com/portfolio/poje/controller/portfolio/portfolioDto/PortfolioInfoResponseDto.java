package com.portfolio.poje.controller.portfolio.portfolioDto;

import com.portfolio.poje.controller.ability.licenseDto.LicenseInfoResponseDto;
import com.portfolio.poje.controller.portfolio.portfolioAwardDto.PortfolioAwardInfoResponseDto;
import com.portfolio.poje.controller.portfolio.portfolioSkillDto.PortfolioSkillInfoResponseDto;
import com.portfolio.poje.controller.project.projectDto.ProjectInfoResponseDto;
import com.portfolio.poje.domain.portfolio.Portfolio;
import com.portfolio.poje.domain.project.Project;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class PortfolioInfoResponseDto {

    // Portfolio
    private String title;

    private String description;

    private String gitHubLink;

    private String blogLink;

    private List<PortfolioAwardInfoResponseDto> portfolioAwardInfoResponseDtoList;

    private List<PortfolioSkillInfoResponseDto> portfolioSkillInfoResponseDtoList;

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
    private List<LicenseInfoResponseDto> licenseInfoResponseDtoList;

    // Project
    private List<ProjectInfoResponseDto> projectInfoResponseDtoList;


    @Builder
    private PortfolioInfoResponseDto(Portfolio portfolio){
        this.title = portfolio.getTitle();
        this.description = portfolio.getDescription();
        this.gitHubLink = portfolio.getGitHubLink();
        this.blogLink = portfolio.getBlogLink();
        this.portfolioAwardInfoResponseDtoList = toPortfolioAwardDto(portfolio);
        this.portfolioSkillInfoResponseDtoList = toPortfolioSkillDto(portfolio);
        this.jobName = portfolio.getJob().getName();

        this.nickName = portfolio.getWriter().getNickName();
        this.email = portfolio.getWriter().getEmail();
        this.phoneNum = portfolio.getWriter().getPhoneNum();
        this.birth = portfolio.getWriter().getBirth();
        this.academic = portfolio.getWriter().getAcademic();
        this.dept = portfolio.getWriter().getDept();
        this.profileImg = portfolio.getWriter().getProfileImg();
        this.licenseInfoResponseDtoList = toLicenseDto(portfolio);
        this.projectInfoResponseDtoList = toProjectDto(portfolio);
    }


    public List<PortfolioAwardInfoResponseDto> toPortfolioAwardDto(Portfolio portfolio){
        return portfolio.getPortfolioAwards().stream()
                .map(award -> PortfolioAwardInfoResponseDto.builder()
                        .name(award.getName())
                        .supervision(award.getSupervision())
                        .grade(award.getGrade())
                        .description(award.getDescription())
                        .build())
                .collect(Collectors.toList());
    }


    public List<PortfolioSkillInfoResponseDto> toPortfolioSkillDto(Portfolio portfolio){
        return portfolio.getPortfolioSkills().stream()
                .map(skill -> new PortfolioSkillInfoResponseDto(skill.getSkill()))
                .collect(Collectors.toList());
    }


    public List<LicenseInfoResponseDto> toLicenseDto(Portfolio portfolio){
        return portfolio.getWriter().getLicenseList().stream()
                .map(license -> new LicenseInfoResponseDto(license.getName()))
                .collect(Collectors.toList());
    }


    public List<ProjectInfoResponseDto> toProjectDto(Portfolio portfolio){
        return portfolio.getProjects().stream()
                .map(project -> ProjectInfoResponseDto.builder()
                        .project(project)
                        .build())
                .collect(Collectors.toList());
    }

}
