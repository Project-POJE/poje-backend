package com.portfolio.poje.domain.portfolio.service;

import com.portfolio.poje.common.PagingDto;
import com.portfolio.poje.common.PagingUtil;
import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
import com.portfolio.poje.config.SecurityUtil;
import com.portfolio.poje.domain.portfolio.dto.PfDto;
import com.portfolio.poje.domain.portfolio.dto.PfLikeDto;
import com.portfolio.poje.domain.portfolio.repository.PortfolioLikeRepository;
import com.portfolio.poje.domain.member.entity.Member;
import com.portfolio.poje.domain.portfolio.entity.Portfolio;
import com.portfolio.poje.domain.portfolio.entity.PortfolioLike;
import com.portfolio.poje.domain.member.repository.MemberRepository;
import com.portfolio.poje.domain.portfolio.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PortfolioLikeService {

    private final MemberRepository memberRepository;
    private final PortfolioRepository portfolioRepository;
    private final PortfolioLikeRepository portfolioLikeRepository;


    /**
     * 포트폴리오 '좋아요' 클릭 (이미 '좋아요'를 눌렀으면 취소)
     * @param portfolioId
     * @return : PfLikeInfoResp
     */
    @Transactional
    public PfLikeDto.PfLikeInfoResp heartPortfolio(Long portfolioId){
        Member member = memberRepository.findByLoginId(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new PojeException(ErrorCode.MEMBER_NOT_FOUND)
        );

        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow(
                () -> new PojeException(ErrorCode.PORTFOLIO_NOT_FOUND)
        );

        boolean likeStatus = false;
        // 이미 '좋아요'를 클릭했으면 취소
        if (portfolioLikeRepository.existsByMemberAndPortfolio(member, portfolio)){
            PortfolioLike portfolioLike = portfolioLikeRepository.findByMemberAndPortfolio(member, portfolio);

            portfolioLikeRepository.delete(portfolioLike);
        } else {
            // '좋아요' 안눌렀으면 클릭
            PortfolioLike portfolioLike = PortfolioLike.builder()
                    .member(member)
                    .portfolio(portfolio)
                    .build();

            portfolioLikeRepository.save(portfolioLike);

            likeStatus = true;
        }

        Long likeCount = portfolioLikeRepository.countByPortfolio(portfolio);

        return new PfLikeDto.PfLikeInfoResp(likeStatus, likeCount);
    }


    /**
     * 좋아요 누른 포트폴리오 목록 반환
     * @param page
     * @return : PfAndMemberListResp
     */
    @Transactional(readOnly = true)
    public PfDto.PfAndMemberListResp likePortfolios(int page){
        Member member = memberRepository.findByLoginId(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new PojeException(ErrorCode.MEMBER_NOT_FOUND)
        );

        // 현재 페이지 번호로 PagingDto 생성
        PagingDto pagingDto = new PagingDto(page);
        // PagingUtil 객체 생성
        PagingUtil pagingUtil = new PagingUtil(portfolioRepository.findPortfolioWhichMemberLike(member).size(), pagingDto);

        // limit 으로 가져올 포트폴리오 리스트
        List<Portfolio> pagingPortfolioList = portfolioRepository.findPortfolioWhichMemberLike(member, pagingDto.limitCalc());

        return PfDto.PfAndMemberListResp.builder()
                .portfolioList(pagingPortfolioList)
                .pagingUtil(pagingUtil)
                .build();
    }

}
