package com.portfolio.poje.domain.ability.repository;

import com.portfolio.poje.domain.ability.entity.License;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LicenseRepository extends JpaRepository<License, Long> {
    boolean existsByName(String name);

    License findByName(String name);
}
