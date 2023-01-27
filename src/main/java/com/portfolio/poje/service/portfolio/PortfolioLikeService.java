package com.portfolio.poje.service.portfolio;

import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
import com.portfolio.poje.config.SecurityUtil;
import com.portfolio.poje.controller.portfolio.portfolioLikeDto.PfLikeInfoResp;
import com.portfolio.poje.domain.member.Member;
import com.portfolio.poje.domain.portfolio.Portfolio;
import com.portfolio.poje.domain.portfolio.PortfolioLike;
import com.portfolio.poje.repository.member.MemberRepository;
import com.portfolio.poje.repository.portfolio.PortfolioLikeRepository;
import com.portfolio.poje.repository.portfolio.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public PfLikeInfoResp heartPortfolio(Long portfolioId){
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

        return new PfLikeInfoResp(likeStatus, likeCount);
    }


}
