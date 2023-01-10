package com.portfolio.poje.domain.project;

import com.portfolio.poje.domain.ability.Skill;
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

    @Enumerated(EnumType.STRING)
    private Skill skill;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;


    @Builder
    private ProjectSkill(Skill skill, Project project){
        this.skill = skill;
        this.project = project;

        project.getProjectSkills().add(this);
    }

}
