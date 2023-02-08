package com.portfolio.poje.domain.portfolio.repository;

import com.portfolio.poje.domain.portfolio.entity.Portfolio;
import com.portfolio.poje.domain.portfolio.entity.PortfolioSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PortfolioSkillRepository extends JpaRepository<PortfolioSkill, Long> {

    @Query(value = "select distinct ps.type from PortfolioSkill ps where ps.portfolio = :portfolio")
    List<String> findDistinctTypeByPortfolio(@Param(value = "portfolio")Portfolio portfolio);

    List<PortfolioSkill> findByPortfolioAndType(Portfolio portfolio, String type);

}
