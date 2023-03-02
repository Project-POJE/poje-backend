package com.portfolio.poje.domain.portfolio.repository;

import com.portfolio.poje.domain.ability.entity.Job;
import com.portfolio.poje.domain.member.entity.Member;
import com.portfolio.poje.domain.portfolio.entity.Portfolio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PortfolioRepository {

    private final EntityManager em;

    public void save(Portfolio portfolio) {
        em.persist(portfolio);
    }

    public Optional<Portfolio> findById(Long id) {
        return Optional.ofNullable(em.find(Portfolio.class, id));
    }

    public void delete(Portfolio portfolio){
        em.remove(portfolio);
    }

    public List<Portfolio> findAll(){
        return em.createQuery("select p from Portfolio p")
                .getResultList();
    }

    public List<Portfolio> findAll(int limit){
        return em.createQuery("select distinct p from Portfolio p order by p.createdDate desc")
                .setFirstResult(limit)
                .setMaxResults(12)
                .getResultList();
    }

    public List<Portfolio> findAllWithKeyword(String keyword){
        return em.createQuery("select distinct p from Portfolio p where p.title like CONCAT('%', :keyword, '%') order by p.createdDate desc")
                .setParameter("keyword", keyword)
                .getResultList();
    }

    public List<Portfolio> findAllWithKeyword(String keyword, int limit){
        return em.createQuery("select distinct p from Portfolio p where p.title like CONCAT('%', :keyword, '%') order by p.createdDate desc")
                .setParameter("keyword", keyword)
                .setFirstResult(limit)
                .setMaxResults(12)
                .getResultList();
    }

    public List<Portfolio> findPortfolioWithJobAndKeyword(Job job, String keyword){
        return em.createQuery("select distinct p from Portfolio p where p.job = :job and p.title like CONCAT('%', :keyword, '%') order by p.createdDate desc")
                .setParameter("job", job)
                .setParameter("keyword", keyword)
                .getResultList();
    }

    public List<Portfolio> findPortfolioWithJob(Job job, int limit){
        return em.createQuery("select distinct p from Portfolio p where p.job = :job order by p.createdDate desc")
                .setParameter("job", job)
                .setFirstResult(limit)
                .setMaxResults(12)
                .getResultList();
    }

    public List<Portfolio> findPortfolioWithJobAndKeyword(Job job, String keyword, int limit){
        return em.createQuery("select distinct p from Portfolio p where p.job = :job and p.title like CONCAT('%', :keyword, '%') order by p.createdDate desc")
                .setParameter("job", job)
                .setParameter("keyword", keyword)
                .setFirstResult(limit)
                .setMaxResults(12)
                .getResultList();
    }

    public List<Portfolio> findPortfolioWhichMemberLike(Member member){
        return em.createQuery("select distinct pl.portfolio from PortfolioLike pl where pl.member = :member order by pl.createdDate desc")
                .setParameter("member", member)
                .getResultList();
    }

    public List<Portfolio> findPortfolioWhichMemberLike(Member member, int limit){
        return em.createQuery("select distinct pl.portfolio from PortfolioLike pl where pl.member = :member order by pl.createdDate desc")
                .setParameter("member", member)
                .setFirstResult(limit)
                .setMaxResults(12)
                .getResultList();
    }

}
