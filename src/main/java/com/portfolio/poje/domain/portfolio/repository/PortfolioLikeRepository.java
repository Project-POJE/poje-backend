package com.portfolio.poje.domain.portfolio.repository;

import com.portfolio.poje.domain.member.entity.Member;
import com.portfolio.poje.domain.portfolio.entity.Portfolio;
import com.portfolio.poje.domain.portfolio.entity.PortfolioLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioLikeRepository extends JpaRepository<PortfolioLike, Long> {

    boolean existsByMemberAndPortfolio(Member member, Portfolio portfolio);
    PortfolioLike findByMemberAndPortfolio(Member member, Portfolio portfolio);
    Long countByPortfolio(Portfolio portfolioId);

}
