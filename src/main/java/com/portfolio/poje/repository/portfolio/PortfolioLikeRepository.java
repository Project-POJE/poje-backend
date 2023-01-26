package com.portfolio.poje.repository.portfolio;

import com.portfolio.poje.domain.member.Member;
import com.portfolio.poje.domain.portfolio.Portfolio;
import com.portfolio.poje.domain.portfolio.PortfolioLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioLikeRepository extends JpaRepository<PortfolioLike, Long> {

    boolean existsByMemberAndPortfolio(Member member, Portfolio portfolio);
    PortfolioLike findByMemberAndPortfolio(Member member, Portfolio portfolio);
    Long countByPortfolio(Portfolio portfolioId);

}
