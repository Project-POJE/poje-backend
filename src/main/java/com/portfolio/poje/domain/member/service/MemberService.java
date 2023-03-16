package com.portfolio.poje.domain.member.service;

import com.portfolio.poje.config.SecurityUtil;
import com.portfolio.poje.config.aws.S3FileUploader;
import com.portfolio.poje.domain.member.dto.MemberDto;
import com.portfolio.poje.domain.member.entity.Member;
import com.portfolio.poje.domain.member.repository.MemberRepository;
import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.portfolio.poje.config.aws.DefaultImage.DEFAULT_PROFILE_IMG;


@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

    private final S3FileUploader fileUploader;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    /**
     * 로그인 아이디 중복 확인
     * @param loginId
     * @return : boolean
     */
    @Transactional(readOnly = true)
    public boolean loginIdCheck(String loginId){
        return memberRepository.existsByLoginId(loginId);
    }


    /**
     * 사용자 정보 반환
     * @param loginId
     * @return : MemberInfoResp
     */
    @Transactional(readOnly = true)
    public MemberDto.MemberInfoResp getMemberInfo(String loginId){
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(
                () -> new PojeException(ErrorCode.MEMBER_NOT_FOUND)
        );

        return MemberDto.MemberInfoResp.builder()
                .member(member)
                .build();
    }


    /**
     * 사용자 정보 수정
     * @param memberUpdateReq
     * @return : MemberInfoResp
     */
    @Transactional
    public MemberDto.MemberInfoResp updateMember(MemberDto.MemberUpdateReq memberUpdateReq, MultipartFile file) throws Exception{
        Member member = memberRepository.findByLoginId(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new PojeException(ErrorCode.MEMBER_NOT_FOUND)
        );

        if (member.getProfileImg().equals(DEFAULT_PROFILE_IMG) && file != null) {     // 기본 이미지 && 전달받은 이미지 o
            member.updateProfileImg(fileUploader.uploadFile(file, "profile"));          // 전달받은 이미지로 변경

        } else if (!member.getProfileImg().equals(DEFAULT_PROFILE_IMG) && file != null){ // 업로드 된 이미지 && 전달받은 이미지 o
            fileUploader.deleteFile(member.getProfileImg(), "profile");             // 이미지 삭제 후 전달받은 이미지로 변경
            member.updateProfileImg(fileUploader.uploadFile(file, "profile"));
        }

        member.updateInfo(memberUpdateReq.getNickName(), memberUpdateReq.getEmail(),
                          memberUpdateReq.getPhoneNum(), memberUpdateReq.getGender(),
                          memberUpdateReq.getAcademic(), memberUpdateReq.getDept(),
                          memberUpdateReq.getBirth(), memberUpdateReq.getGitHubLink(),
                          memberUpdateReq.getBlogLink());

        return MemberDto.MemberInfoResp.builder()
                .member(member)
                .build();
    }


    /**
     * 비밀번호 변경
     * @param passwordUpdateReq
     */
    @Transactional
    public void changePassword(MemberDto.PasswordUpdateReq passwordUpdateReq){
        Member member = memberRepository.findByLoginId(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new PojeException(ErrorCode.MEMBER_NOT_FOUND)
        );

        if (passwordEncoder.matches(passwordUpdateReq.getExistPassword(), member.getPassword())) {
            updatePassword(member, passwordUpdateReq.getNewPassword());
        } else {
            throw new PojeException(ErrorCode.PASSWORD_NOT_MATCH);
        }
    }


    /**
     * 입력 정보 일치 여부 확인
     * @param passwordFindReq
     * @return member
     */
    @Transactional(readOnly = true)
    public Member checkPassword(MemberDto.PasswordFindReq passwordFindReq){
        Member member = memberRepository.findByEmail(passwordFindReq.getEmail()).orElseThrow(
                () -> new PojeException(ErrorCode.MEMBER_NOT_FOUND)
        );

        // 회원 정보가 일치하지 않으면 예외
        if (member.getNickName().equals(passwordFindReq.getNickName())) {
            return member;
        } else {
            throw new PojeException(ErrorCode.MEMBER_INFO_NOT_MATCH);
        }
    }


    /**
     * 비밀번호 업데이트
     * @param member
     * @param password
     */
    @Transactional
    public void updatePassword(Member member, String password){
        member.updatePassword(passwordEncoder.encode(password));
        memberRepository.save(member);
    }

}