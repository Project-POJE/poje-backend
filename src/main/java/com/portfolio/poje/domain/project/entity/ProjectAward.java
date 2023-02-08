package com.portfolio.poje.domain.project.entity;

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

    private String supervision;

    private String grade;

    private String description;


    @Builder(builderMethodName = "enrollProjectAward")
    private ProjectAward(String supervision, String grade, String description, Project project){
        this.supervision = supervision;
        this.grade = grade;
        this.description = description;

        project.insertAward(this);
    }

    public void updateInfo(String supervision, String grade, String description){
        if (supervision != null) this.supervision = supervision;
        if (grade != null) this.grade = grade;
        if (description != null) this.description = description;
    }

}
