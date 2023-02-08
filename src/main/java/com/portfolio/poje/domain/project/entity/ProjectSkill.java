package com.portfolio.poje.domain.project.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ProjectSkill {

    @Id @GeneratedValue
    @Column(name = "project_skill_id")
    private Long id;

    private String type;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;


    @Builder
    private ProjectSkill(String type, String name, Project project){
        this.type = type;
        this.name = name;
        this.project = project;

        project.getProjectSkills().add(this);
    }

}
