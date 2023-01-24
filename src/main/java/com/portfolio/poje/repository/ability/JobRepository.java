package com.portfolio.poje.repository.ability;

import com.portfolio.poje.domain.ability.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
}
