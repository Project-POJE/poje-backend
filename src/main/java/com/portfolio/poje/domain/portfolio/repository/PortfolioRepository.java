package com.portfolio.poje.domain.portfolio.repository;

import com.portfolio.poje.domain.portfolio.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
}
