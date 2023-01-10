package com.portfolio.poje.domain.portfolio;

import com.portfolio.poje.domain.project.Project;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PortfolioAward {

    @Id
    @GeneratedValue
    @Column(name = "portfolio_award_id")
    private Long id;

    private String name;

    private String supervision;

    private String grade;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;


    @Builder(builderMethodName = "enrollPortfolioAward")
    private PortfolioAward(String name, String supervision, String grade, String description, Portfolio portfolio){
        this.name = name;
        this.supervision = supervision;
        this.grade = grade;
        this.description = description;
        this.portfolio = portfolio;

        portfolio.getPortfolioAwards().add(this);
    }

}
