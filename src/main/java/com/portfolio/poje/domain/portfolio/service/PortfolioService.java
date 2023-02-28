package com.portfolio.poje.domain.portfolio.service;

import com.portfolio.poje.common.PagingDto;
import com.portfolio.poje.common.PagingUtil;
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

import java.util.List;

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
     * 포트폴리오 수정 권한 여부 응답
     * @param portfolioId
     * @return : boolean
     */
    @Transactional(readOnly = true)
    public boolean getPermission(Long portfolioId){
        Member member = memberRepository.findByLoginId(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new PojeException(ErrorCode.MEMBER_NOT_FOUND)
        );

        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow(
                () -> new PojeException(ErrorCode.PORTFOLIO_NOT_FOUND)
        );

        // 요청받은 포트폴리오가 사용자가 작성한 목록에 포함되어 있으면 true
        if (member.getPortfolioList().contains(portfolio)){
            return true;
        } else {
            return false;
        }
    }


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
     * 직무명이 '전체' 일 때 직무 별 포트폴리오 & 작성자 정보 목록 반환
     * @param page
     * @param keyword
     * @return : PfAndMemberListResp
     */
    @Transactional(readOnly = true)
    public PfDto.PfAndMemberListResp getPortfolios(int page, String keyword){
        // 현재 페이지 번호로 PagingDto 생성
        PagingDto pagingDto = new PagingDto(page);
        // PagingUtil 객체 생성
        PagingUtil pagingUtil;

        // limit 으로 가져올 포트폴리오 리스트
        List<Portfolio> pagingPortfolioList;

        if (keyword.equals("")){
            // 요청 받은 검색 키워드가 없을 때
            pagingUtil = new PagingUtil(portfolioRepository.findAll().size(), pagingDto);
            pagingPortfolioList = portfolioRepository.findAll(pagingDto.limitCalc());
        } else {
            // 요청 받은 검색 키워드가 있을 때
            pagingUtil = new PagingUtil(portfolioRepository.findAllWithKeyword(keyword).size(), pagingDto);
            pagingPortfolioList = portfolioRepository.findAllWithKeyword(keyword, pagingDto.limitCalc());
        }

        return PfDto.PfAndMemberListResp.builder()
                .portfolioList(pagingPortfolioList)
                .pagingUtil(pagingUtil)
                .build();
    }


    /**
     * 직무 별 포트폴리오 & 작성자 정보 목록 반환
     * @param jobName
     * @param page
     * @param keyword
     * @return : PfAndMemberListResp
     */
    @Transactional(readOnly = true)
    public PfDto.PfAndMemberListResp getPortfoliosWithJob(String jobName, int page, String keyword){
        Job job = jobRepository.findByName(jobName).orElseThrow(
                () -> new PojeException(ErrorCode.JOB_NOT_FOUND)
        );

        // 현재 페이지 번호로 PagingDto 생성
        PagingDto pagingDto = new PagingDto(page);
        // PagingUtil 객체 생성
        PagingUtil pagingUtil;

        // limit 으로 가져온 직무별 포트폴리오 리스트
        List<Portfolio> pagingPortfolioList;

        if (keyword.equals("")){
            pagingUtil = new PagingUtil(job.getPortfolioList().size(), pagingDto);
            pagingPortfolioList = portfolioRepository.findPortfoliosWithJob(job, pagingDto.limitCalc());
        } else {
            pagingUtil = new PagingUtil(portfolioRepository.findPortfoliosWithJobAndKeyword(job, keyword).size(), pagingDto);
            pagingPortfolioList = portfolioRepository.findPortfoliosWithJobAndKeyword(job, keyword, pagingDto.limitCalc());
        }

        return PfDto.PfAndMemberListResp.builder()
                .portfolioList(pagingPortfolioList)
                .pagingUtil(pagingUtil)
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


    /**
     * 내 포트폴리오 정보 목록 반환
     * @return : PfAndMemberListResp
     */
    @Transactional(readOnly = true)
    public PfDto.PfAndMemberListResp getMyPortfolios(){
        Member member = memberRepository.findByLoginId(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new PojeException(ErrorCode.MEMBER_NOT_FOUND)
        );

        return PfDto.PfAndMemberListResp.builder()
                .portfolioList(member.getPortfolioList())
                .build();
    }


    /**
     * 포트폴리오 삭제
     * @param portfolioId
     */
    @Transactional
    public void deletePortfolio(Long portfolioId){
        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow(
                () -> new PojeException(ErrorCode.PORTFOLIO_NOT_FOUND)
        );

        portfolioRepository.delete(portfolio);
    }


}
