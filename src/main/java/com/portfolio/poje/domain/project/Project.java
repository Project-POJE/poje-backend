package com.portfolio.poje.domain.project;

import com.portfolio.poje.common.BaseEntity;
import com.portfolio.poje.domain.portfolio.Portfolio;
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
public class Project extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "project_id")
    private Long id;

    private String name;

    private String duration;

    private String description;

    private String belong;

    private String link;

    @OneToMany(mappedBy = "project", orphanRemoval = true)
    private List<ProjectImg> projectImgs = new ArrayList<>();

    @OneToMany(mappedBy = "project", orphanRemoval = true)
    private List<ProjectSkill> projectSkills = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_award_id")
    private ProjectAward projectAward;


    @Builder(builderMethodName = "createProject")
    private Project(String name, String duration, String description, String belong, String link, Portfolio portfolio){
        this.name = name;
        this.duration = duration;
        this.description = description;
        this.belong = belong;
        this.link = link;
        this.portfolio = portfolio;

        portfolio.getProjects().add(this);
    }

    public void insertAward(ProjectAward projectAward){
        this.projectAward = projectAward;
    }

    public void updateInfo(String name, String duration, String description, String belong, String link){
        if (name != null) this.name = name;
        if (duration != null) this.duration = duration;
        if (description != null) this.description = description;
        if (belong != null) this.belong = belong;
        if (link != null) this.link = link;
    }

}
