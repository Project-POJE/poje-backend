package com.portfolio.poje.domain.project;

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

    private String skill;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;


    @Builder
    private ProjectSkill(String type, String skill, Project project){
        this.type = type;
        this.skill = skill;
        this.project = project;

        project.getProjectSkills().add(this);
    }

}
