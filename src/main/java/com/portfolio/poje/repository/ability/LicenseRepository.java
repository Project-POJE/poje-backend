package com.portfolio.poje.repository.ability;

import com.portfolio.poje.domain.ability.License;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LicenseRepository extends JpaRepository<License, Long> {
    boolean existsByName(String name);

    void deleteByName(String name);
}
