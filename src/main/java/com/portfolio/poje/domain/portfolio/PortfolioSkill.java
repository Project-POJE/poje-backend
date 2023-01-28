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

    private String name;

    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;


    @Builder
    private PortfolioSkill(String type, String name, String path, Portfolio portfolio){
        this.type = type;
        this.name = name;
        this.path = path;
        this.portfolio = portfolio;

        portfolio.getPortfolioSkills().add(this);
    }

}
