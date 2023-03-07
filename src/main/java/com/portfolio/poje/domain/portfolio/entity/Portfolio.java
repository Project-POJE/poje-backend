package com.portfolio.poje.domain.portfolio.entity;

import com.portfolio.poje.common.BaseEntity;
import com.portfolio.poje.domain.ability.entity.Job;
import com.portfolio.poje.domain.member.entity.Member;
import com.portfolio.poje.domain.project.entity.Project;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Portfolio extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "portfolio_id")
    private Long id;

    private String title;

    private String description;

    private String backgroundImg;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;

    @OneToMany(mappedBy = "portfolio", orphanRemoval = true)
    private List<PortfolioSkill> portfolioSkills = new ArrayList<>();

    @OneToMany(mappedBy = "portfolio", orphanRemoval = true)
    private List<Project> projects = new ArrayList<>();

    @OneToMany(mappedBy = "portfolio", orphanRemoval = true)
    private List<PortfolioLike> likes = new ArrayList<>();

    @OneToMany(mappedBy = "portfolio", orphanRemoval = true)
    private List<PortfolioAward> portfolioAwards = new ArrayList<>();


    @Builder(builderMethodName = "createPortfolio")
    private Portfolio(String title, String description, Member writer, Job job, String backgroundImg){
        this.title = title;
        this.description = description;
        this.writer = writer;
        this.job = job;
        this.backgroundImg = backgroundImg;

        writer.getPortfolioList().add(this);
        job.getPortfolioList().add(this);
    }


    public void updatePortfolio(String title, String description){
        if (title != null) this.title = title;
        if (description != null) this.description = description;
    }

    public void updateBackgroundImg(String backgroundImg){
        this.backgroundImg = backgroundImg;
    }

}
