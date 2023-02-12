package com.portfolio.poje.domain.portfolio.dto.portfolioDto;

import com.portfolio.poje.domain.portfolio.entity.Portfolio;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PfAboutMeResp {

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
