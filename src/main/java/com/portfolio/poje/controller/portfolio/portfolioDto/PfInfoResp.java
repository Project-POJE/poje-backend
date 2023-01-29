package com.portfolio.poje.controller.portfolio.portfolioDto;

import com.portfolio.poje.controller.ability.licenseDto.LicenseInfoResp;
import com.portfolio.poje.controller.portfolio.portfolioAwardDto.PfAwardInfoResp;
import com.portfolio.poje.controller.portfolio.portfolioSkillDto.PfSkillInfoResp;
import com.portfolio.poje.controller.project.projectDto.PrInfoResp;
import com.portfolio.poje.domain.portfolio.Portfolio;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class PfInfoResp {

    // Portfolio
    private Long portfolioId;

    private String title;

    private String description;

    private String gitHubLink;

    private String blogLink;

    private String backgroundImg;

    private List<PfAwardInfoResp> pfAwardInfoRespList;

    private List<PfSkillInfoResp> pfSkillInfoRespList;

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
    private List<LicenseInfoResp> licenseInfoRespList;

    // Project
    private List<PrInfoResp> prInfoRespList;


    @Builder
    private PfInfoResp(Portfolio portfolio){
        this.portfolioId = portfolio.getId();
        this.title = portfolio.getTitle();
        this.description = portfolio.getDescription();
        this.gitHubLink = portfolio.getGitHubLink();
        this.blogLink = portfolio.getBlogLink();
        this.backgroundImg = portfolio.getBackgroundImg();
        this.pfAwardInfoRespList = toPortfolioAwardDto(portfolio);
        this.pfSkillInfoRespList = toPortfolioSkillDto(portfolio);
        this.jobName = portfolio.getJob().getName();

        this.nickName = portfolio.getWriter().getNickName();
        this.email = portfolio.getWriter().getEmail();
        this.phoneNum = portfolio.getWriter().getPhoneNum();
        this.birth = portfolio.getWriter().getBirth();
        this.academic = portfolio.getWriter().getAcademic();
        this.dept = portfolio.getWriter().getDept();
        this.profileImg = portfolio.getWriter().getProfileImg();
        this.licenseInfoRespList = toLicenseDto(portfolio);
        this.prInfoRespList = toProjectDto(portfolio);
    }


    public List<PfAwardInfoResp> toPortfolioAwardDto(Portfolio portfolio){
        return portfolio.getPortfolioAwards().stream()
                .map(award -> PfAwardInfoResp.builder()
                        .supervision(award.getSupervision())
                        .grade(award.getGrade())
                        .description(award.getDescription())
                        .build())
                .collect(Collectors.toList());
    }


    public List<PfSkillInfoResp> toPortfolioSkillDto(Portfolio portfolio){
        return portfolio.getPortfolioSkills().stream()
                .map(skill -> new PfSkillInfoResp(skill.getType(), skill.getName(), skill.getPath()))
                .collect(Collectors.toList());
    }


    public List<LicenseInfoResp> toLicenseDto(Portfolio portfolio){
        return portfolio.getWriter().getLicenseList().stream()
                .map(license -> new LicenseInfoResp(license.getName()))
                .collect(Collectors.toList());
    }


    public List<PrInfoResp> toProjectDto(Portfolio portfolio){
        return portfolio.getProjects().stream()
                .map(project -> PrInfoResp.builder()
                        .project(project)
                        .build())
                .collect(Collectors.toList());
    }

}
