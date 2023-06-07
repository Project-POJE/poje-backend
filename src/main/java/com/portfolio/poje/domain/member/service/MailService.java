package com.portfolio.poje.domain.member.service;

import com.portfolio.poje.domain.member.dto.MemberDto;

public interface MailService {

    /**
     * 임시 비밀번호 생성 후 반환
     * @return String
     */
    String issueTempPassword();


    /**
     * 임시 비밀번호를 담은 메일 생성 후 전송
     * @param passwordFindReq
     * @param tempPassword
     */
    void createMailAndSend(MemberDto.PasswordFindReq passwordFindReq, String tempPassword);
}
