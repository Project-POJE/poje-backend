package com.portfolio.poje.domain.member.service;

import com.portfolio.poje.domain.member.dto.MailDto;
import com.portfolio.poje.domain.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MailService {

    private final JavaMailSender javaMailSender;

    /**
     * 임시 비밀번호 생성 후 반환
     * @return : String
     */
    public String issueTempPassword(){
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        StringBuilder sb = new StringBuilder();

        // 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 구문을 작성함
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            sb.append(charSet[idx]);
        }

        log.info("임시 비밀번호 생성");
        return sb.toString();
    }


    /**
     * 임시 비밀번호를 담은 메일 생성 후 전송
     * @param passwordFindReq
     */
    public void createMailAndSend(MemberDto.PasswordFindReq passwordFindReq, String tempPassword) {
        String emailAddress = passwordFindReq.getEmail();
        String title = "POJE 임시 비밀번호 안내 이메일 입니다.";
        String message = "POJE 임시 비밀번호 안내 관련 이메일 입니다.\n" +
                "회원님의 임시 비밀번호는 " + tempPassword + " 입니다.\n" +
                "로그인 후에 비밀번호를 변경을 해주세요";

        MailDto mailDto = new MailDto(emailAddress, title, message);

        // 전송할 메일 생성
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(mailDto.getEmailAddress());
        mail.setSubject(mailDto.getTitle());
        mail.setText(mailDto.getMessage());
        mail.setFrom("msu122005@hs.ac.kr");

        javaMailSender.send(mail);

        log.info("메일 전송 완료");
    }
}
