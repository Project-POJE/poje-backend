package com.portfolio.poje.domain.member.controller;

import com.portfolio.poje.common.BasicResponse;
import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
import com.portfolio.poje.config.SecurityUtil;
import com.portfolio.poje.domain.member.dto.MemberDto;
import com.portfolio.poje.domain.member.entity.Member;
import com.portfolio.poje.domain.member.service.MailService;
import com.portfolio.poje.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;
    private final MailService mailService;


    /**
     * 로그인 아이디 중복 확인
     * @param loginId
     * @return
     */
    @GetMapping("/loginId/{loginId}")
    public ResponseEntity<BasicResponse> loginIdDuplicate(@PathVariable(value = "loginId") String loginId){
        // 중복이면 예외
        if (memberService.loginIdCheck(loginId)) {
            throw new PojeException(ErrorCode.ID_ALREADY_EXIST);
        }

        BasicResponse basicResponse = new BasicResponse(HttpStatus.OK.value(), "사용할 수 있는 아이디입니다.");

        return ResponseEntity.ok(basicResponse);
    }


    /**
     * 사용자 정보 반환
     * @return : MemberInfoResp
     */
    @GetMapping("/member")
    public ResponseEntity<BasicResponse> getMemberInfo(){
        MemberDto.MemberInfoResp memberInfoResp = memberService.getMemberInfo(SecurityUtil.getCurrentMemberId());

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "회원 정보 조회", memberInfoResp));
    }


    /**
     * 사용자 정보 수정
     * @param memberUpdateReq
     * @param file
     * @return : MemberInfoResp
     * @throws Exception
     */
    @PutMapping("/member")
    public ResponseEntity<BasicResponse> updateMemberInfo(@RequestPart(value = "memberUpdateReq") @Valid MemberDto.MemberUpdateReq memberUpdateReq,
                                                          @RequestPart(value = "profileImg", required = false)MultipartFile file) throws Exception{
        MemberDto.MemberInfoResp memberInfoResp = memberService.updateMember(memberUpdateReq, file);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "회원 정보가 수정되었습니다.", memberInfoResp));
    }


    /**
     * 비밀번호 변경
     * @param passwordUpdateReq
     * @return
     */
    @PutMapping("/member/password")
    public ResponseEntity<BasicResponse> updatePassword(@RequestBody @Valid MemberDto.PasswordUpdateReq passwordUpdateReq){
        memberService.changePassword(passwordUpdateReq);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "비밀번호가 변경되었습니다."));
    }


    /**
     * 비밀번호 찾기
     * @param passwordFindReq
     * @return
     */
    @PostMapping("/find/password")
    public ResponseEntity<BasicResponse> checkPassword(@RequestBody @Valid MemberDto.PasswordFindReq passwordFindReq){
        // 입력 정보 일치 여부 확인
        Member member = memberService.checkPassword(passwordFindReq);

        // 임시 비밀번호 발급 및 변경
        String tempPassword = mailService.issueTempPassword();
        memberService.updatePassword(member, tempPassword);

        // 입력한 이메일로 임시 비밀번호를 담은 메일 전송
        mailService.createMailAndSend(passwordFindReq, tempPassword);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "임시 비밀번호를 담은 메일이 전송되었습니다."));
    }

}
