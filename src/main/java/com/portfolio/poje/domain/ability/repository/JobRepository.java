package com.portfolio.poje.domain.ability.repository;

import com.portfolio.poje.domain.ability.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {

    Optional<Job> findByName(String name);
}
