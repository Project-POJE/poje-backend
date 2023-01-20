package com.portfolio.poje.domain.project;

import com.portfolio.poje.domain.portfolio.Portfolio;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ProjectAward {

    @Id @GeneratedValue
    @Column(name = "project_award_id")
    private Long id;

    private String name;

    private String supervision;

    private String grade;

    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Builder(builderMethodName = "enrollProjectAward")
    private ProjectAward(String name, String supervision, String grade, String description, Project project){
        this.name = name;
        this.supervision = supervision;
        this.grade = grade;
        this.description = description;
        this.project = project;

        project.insertAward(this);
    }

    public void updateInfo(String name, String supervision, String grade, String description){
        if (name != null) this.name = name;
        if (supervision != null) this.supervision = supervision;
        if (grade != null) this.grade = grade;
        if (description != null) this.description = description;
    }

}
