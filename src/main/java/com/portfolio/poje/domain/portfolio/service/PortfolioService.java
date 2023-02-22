package com.portfolio.poje.domain.portfolio.service;

import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
import com.portfolio.poje.config.SecurityUtil;
import com.portfolio.poje.config.aws.S3FileUploader;
import com.portfolio.poje.domain.ability.entity.Job;
import com.portfolio.poje.domain.member.entity.Member;
import com.portfolio.poje.domain.portfolio.dto.PfDto;
import com.portfolio.poje.domain.portfolio.entity.Portfolio;
import com.portfolio.poje.domain.ability.repository.JobRepository;
import com.portfolio.poje.domain.member.repository.MemberRepository;
import com.portfolio.poje.domain.portfolio.repository.PortfolioLikeRepository;
import com.portfolio.poje.domain.portfolio.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.portfolio.poje.config.aws.DefaultImage.DEFAULT_PORTFOLIO_IMG;


@Slf4j
@RequiredArgsConstructor
@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final PortfolioLikeRepository portfolioLikeRepository;
    private final MemberRepository memberRepository;
    private final JobRepository jobRepository;
    private final S3FileUploader fileUploader;

    /**
     * 기본 정보만 담은 포트폴리오 생성
     * @param jobName
     */
    @Transactional
    public PfDto.PfCreateResp enrollBasicPortfolio(String jobName){
        if(jobName.equals("전체")){
            throw new PojeException(ErrorCode.JOB_ENTIRE_CANNOT_GENERATE);
        }

        Member member = memberRepository.findByLoginId(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new PojeException(ErrorCode.MEMBER_NOT_FOUND)
        );

        Job job = jobRepository.findByName(jobName).orElseThrow(
                () -> new PojeException(ErrorCode.JOB_NOT_FOUND)
        );

        Portfolio portfolio = Portfolio.createPortfolio()
                .title("제목을 입력해주세요.")
                .description("내용을 입력해주세요.")
                .writer(member)
                .job(job)
                .backgroundImg(DEFAULT_PORTFOLIO_IMG)
                .build();

        portfolioRepository.save(portfolio);

        return new PfDto.PfCreateResp(portfolio.getId());
    }


    /**
     * 포트폴리오 정보 수정
     * @param portfolioId
     * @param pfUpdateReq
     * @param file
     * @return : PfInfoResp
     * @throws Exception
     */
    @Transactional
    public PfDto.PfInfoResp updatePortfolioInfo(Long portfolioId, PfDto.PfUpdateReq pfUpdateReq, MultipartFile file) throws Exception{
        Member member = memberRepository.findByLoginId(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new PojeException(ErrorCode.MEMBER_NOT_FOUND)
        );

        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow(
                () -> new PojeException(ErrorCode.PORTFOLIO_NOT_FOUND)
        );

        if (portfolio.getBackgroundImg().equals(DEFAULT_PORTFOLIO_IMG) && file != null){    // 기본 이미지 && 전달받은 이미지 o
            portfolio.updateBackgroundImg(fileUploader.uploadFile(file, "portfolio"));      // 전달받은 이미지로 변경

        } else if (!portfolio.getBackgroundImg().equals(DEFAULT_PORTFOLIO_IMG) && file != null){   // 업로드 된 이미지 && 전달받은 이미지 o
            fileUploader.deleteFile(portfolio.getBackgroundImg(), "portfolio");             // 이미지 삭제 후 전달받은 이미지로 변경
            portfolio.updateBackgroundImg(fileUploader.uploadFile(file, "portfolio"));
        }

        portfolio.updatePortfolio(pfUpdateReq.getTitle(), pfUpdateReq.getDescription());

        // 포트폴리오 좋아요 눌렀는지 여부
        boolean likeStatus = portfolioLikeRepository.existsByMemberAndPortfolio(member, portfolio);

        return PfDto.PfInfoResp.builder()
                .portfolio(portfolio)
                .likeStatus(likeStatus)
                .build();
    }


    /**
     * 직무 별 포트폴리오 & 작성자 정보 목록 반환
     * @param jobName
     * @return : PfAndMemberListResp
     */
    @Transactional(readOnly = true)
    public PfDto.PfAndMemberListResp getPortfoliosWithJob(String jobName){
        Member member = memberRepository.findByLoginId(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new PojeException(ErrorCode.MEMBER_NOT_FOUND)
        );

        Map<Portfolio, Boolean> portfolioMap = new HashMap<>();
        List<Portfolio> portfolioList;

        if (jobName.equals("전체")){
            // 직무명이 '전체'일 때 모든 포트폴리오 정보 반환
            portfolioList = portfolioRepository.findAll();
        } else {
            Job job = jobRepository.findByName(jobName).orElseThrow(
                    () -> new PojeException(ErrorCode.JOB_NOT_FOUND)
            );

            portfolioList = job.getPortfolioList();
        }

        // Portfolio 목록을 뒤져 Map에 넣어줌
        for (Portfolio portfolio : portfolioList){
            // 포트폴리오에 좋아요 눌렀는지 여부
            boolean likeStatus = portfolioLikeRepository.existsByMemberAndPortfolio(member, portfolio);
            portfolioMap.put(portfolio, likeStatus);
        }

        return PfDto.PfAndMemberListResp.builder()
                .portfolioMap(portfolioMap)
                .build();
    }


    /**
     * 포트폴리오 정보 반환
     * @param portfolioId
     * @return : PfInfoResp
     */
    @Transactional(readOnly = true)
    public PfDto.PfInfoResp getPortfolioInfo(Long portfolioId){
        Member member = memberRepository.findByLoginId(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new PojeException(ErrorCode.MEMBER_NOT_FOUND)
        );

        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow(
                () -> new PojeException(ErrorCode.PORTFOLIO_NOT_FOUND)
        );

        // 포트폴리오 좋아요 눌렀는지 여부
        boolean likeStatus = portfolioLikeRepository.existsByMemberAndPortfolio(member, portfolio);

        return PfDto.PfInfoResp.builder()
                .portfolio(portfolio)
                .likeStatus(likeStatus)
                .build();
    }


    /**
     * 포트폴리오 About Me 정보 반환
     * @param portfolioId
     * @return : PfAboutMeResp
     */
    @Transactional(readOnly = true)
    public PfDto.PfAboutMeResp getPortfolioAboutMe(Long portfolioId){
        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow(
                () -> new PojeException(ErrorCode.PORTFOLIO_NOT_FOUND)
        );

        return PfDto.PfAboutMeResp.builder()
                .portfolio(portfolio)
                .build();
    }



}
