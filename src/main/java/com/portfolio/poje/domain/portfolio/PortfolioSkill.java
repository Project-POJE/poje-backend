package com.portfolio.poje.domain.portfolio;

import com.portfolio.poje.domain.ability.Skill;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PortfolioSkill {

    @Id @GeneratedValue
    @Column(name = "portfolio_skill_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Skill skill;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;


    @Builder
    private PortfolioSkill(Skill skill, Portfolio portfolio){
        this.skill = skill;
        this.portfolio = portfolio;

        portfolio.getPortfolioSkills().add(this);
    }

}
