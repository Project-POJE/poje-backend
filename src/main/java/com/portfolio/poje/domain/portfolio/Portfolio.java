package com.portfolio.poje.domain.portfolio;

import com.portfolio.poje.common.BaseEntity;
import com.portfolio.poje.domain.ability.Job;
import com.portfolio.poje.domain.member.Member;
import com.portfolio.poje.domain.project.Project;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;

    @OneToMany(mappedBy = "portfolio")
    private List<Note> notes = new ArrayList<>();

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL)
    private List<PortfolioSkill> portfolioSkills = new ArrayList<>();

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL)
    private List<Project> projects = new ArrayList<>();

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL)
    private List<PortfolioLike> likes = new ArrayList<>();

    @OneToMany(mappedBy = "portfolio")
    private List<PortfolioAward> portfolioAwards = new ArrayList<>();


    @Builder(builderMethodName = "createPortfolio")
    private Portfolio(String title, String description, Member writer, Job job){
        this.title = title;
        this.description = description;
        this.writer = writer;
        this.job = job;

        writer.getPortfolioList().add(this);
        job.getPortfolioList().add(this);
    }

}
