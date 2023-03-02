package com.portfolio.poje.domain.member.repository;

import com.portfolio.poje.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByLoginId(String loginId);

    boolean existsByLoginId(String loginId);

    Optional<Member> findByEmail(String email);

}
