package com.portfolio.poje.domain.portfolio;

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

    private String type;

    private String skill;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;


    @Builder
    private PortfolioSkill(String type, String skill, Portfolio portfolio){
        this.type = type;
        this.skill = skill;
        this.portfolio = portfolio;

        portfolio.getPortfolioSkills().add(this);
    }

}
