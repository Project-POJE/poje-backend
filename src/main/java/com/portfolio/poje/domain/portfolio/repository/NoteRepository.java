package com.portfolio.poje.domain.portfolio.repository;

import com.portfolio.poje.domain.member.entity.Member;
import com.portfolio.poje.domain.portfolio.entity.Note;
import com.portfolio.poje.domain.portfolio.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {

    @Query(value = "select n from Note n where (n.sender = :member or n.receiver = :member) and n.portfolio = :portfolio order by n.createdDate desc")
    List<Note> findByMemberAndPortfolio(@Param(value = "member") Member member, @Param(value = "portfolio") Portfolio portfolio);

    @Query(value = "select n from Note n where n.sender = :member or n.receiver = :member order by n.createdDate desc")
    List<Note> findByMember(@Param(value = "member") Member member);
}
