package com.portfolio.poje.repository.portfolio;

import com.portfolio.poje.domain.portfolio.PortfolioSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PortfolioSkillRepository extends JpaRepository<PortfolioSkill, Long> {

    @Query(value = "select distinct ps.type from PortfolioSkill ps where ps.portfolio.id = :id")
    List<String> findDistinctTypeById(@Param(value = "id") Long id);

    List<PortfolioSkill> findByPortfolioIdAndType(Long id, String type);

}
