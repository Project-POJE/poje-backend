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


    @Builder(builderMethodName = "enrollProjectAward")
    private ProjectAward(String name, String supervision, String grade, String description){
        this.name = name;
        this.supervision = supervision;
        this.grade = grade;
        this.description = description;
    }

}
