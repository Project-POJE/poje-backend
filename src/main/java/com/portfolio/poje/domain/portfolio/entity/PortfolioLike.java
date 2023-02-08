package com.portfolio.poje.domain.portfolio.entity;

import com.portfolio.poje.common.BaseEntity;
import com.portfolio.poje.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PortfolioLike extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "portfolio_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;


    @Builder
    private PortfolioLike(Member member, Portfolio portfolio){
        this.member = member;
        this.portfolio = portfolio;

        portfolio.getLikes().add(this);
    }

}
