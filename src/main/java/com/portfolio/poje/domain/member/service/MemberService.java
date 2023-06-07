package com.portfolio.poje.domain.member.service;

import com.portfolio.poje.domain.member.dto.MemberDto;
import com.portfolio.poje.domain.member.entity.Member;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MemberService {

    /**
     * 로그인 아이디 중복 확인
     * @param loginId
     * @return boolean
     */
    boolean loginIdCheck(String loginId);


    /**
     * 사용자 정보 반환
     * @param loginId
     * @return MemberInfoResp
     */
    MemberDto.MemberInfoResp getMemberInfo(String loginId);


    /**
     * 사용자 정보 수정
     * @param memberUpdateReq
     * @param file
     * @return MemberInfoResp
     * @throws Exception
     */
    MemberDto.MemberInfoResp updateMember(MemberDto.MemberUpdateReq memberUpdateReq, MultipartFile file) throws IOException;


    /**
     * 비밀번호 변경
     * @param passwordUpdateReq
     */
    void changePassword(MemberDto.PasswordUpdateReq passwordUpdateReq);


    /**
     * 입력 정보 일치 여부 확인
     * @param passwordFindReq
     * @return Member
     */
    Member checkPassword(MemberDto.PasswordFindReq passwordFindReq);


    /**
     * 비밀번호 업데이트
     * @param member
     * @param password
     */
    void updatePassword(Member member, String password);
}
