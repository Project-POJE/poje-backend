package com.portfolio.poje.domain.portfolio.service;

import com.portfolio.poje.domain.portfolio.dto.PfDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PortfolioService {

    /**
     * 포트폴리오 수정 권한 여부 응답
     * @param portfolioId
     * @return boolean
     */
    boolean getPermission(Long portfolioId);


    /**
     * 기본 정보만 담은 포트폴리오 생성
     * @param jobName
     * @return PfCreateResp
     */
    PfDto.PfCreateResp enrollBasicPortfolio(String jobName);


    /**
     * 포트폴리오 정보 수정
     * @param portfolioId
     * @param pfUpdateReq
     * @param file
     * @return PfInfoResp
     * @throws IOException
     */
    PfDto.PfInfoResp updatePortfolioInfo(Long portfolioId, PfDto.PfUpdateReq pfUpdateReq, MultipartFile file) throws IOException;


    /**
     * 직무명이 '전체' 일 때 직무 별 포트폴리오 & 작성자 정보 목록 반환
     * @param page
     * @param keyword
     * @return PfAndMemberListResp
     */
    PfDto.PfAndMemberListResp getPortfolios(int page, String keyword);


    /**
     * 직무 별 포트폴리오 & 작성자 정보 목록 반환
     * @param jobName
     * @param page
     * @param keyword
     * @return PfAndMemberListResp
     */
    PfDto.PfAndMemberListResp getPortfoliosWithJob(String jobName, int page, String keyword);


    /**
     * 포트폴리오 정보 반환
     * @param portfolioId
     * @return PfInfoResp
     */
    PfDto.PfInfoResp getPortfolioInfo(Long portfolioId);


    /**
     * 포트폴리오 About Me 정보 반환
     * @param portfolioId
     * @return PfAboutMeResp
     */
    PfDto.PfAboutMeResp getPortfolioAboutMe(Long portfolioId);


    /**
     * 내 포트폴리오 정보 목록 반환
     * @return PfAndMemberListResp
     */
    PfDto.PfAndMemberListResp getMyPortfolios();


    /**
     * 포트폴리오 삭제
     * @param portfolioId
     */
    void deletePortfolio(Long portfolioId);
}
