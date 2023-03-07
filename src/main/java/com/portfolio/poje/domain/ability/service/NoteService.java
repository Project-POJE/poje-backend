package com.portfolio.poje.domain.ability.service;

import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
import com.portfolio.poje.config.SecurityUtil;
import com.portfolio.poje.domain.ability.dto.NoteDto;
import com.portfolio.poje.domain.ability.entity.Note;
import com.portfolio.poje.domain.ability.entity.NoteStatus;
import com.portfolio.poje.domain.ability.repository.NoteRepository;
import com.portfolio.poje.domain.member.entity.Member;
import com.portfolio.poje.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class NoteService {

    private final MemberRepository memberRepository;

    private final NoteRepository noteRepository;


    /**
     * 닉네임으로 쪽지 전송
     * @param noteSendReq
     * @return : NoteInfoResp
     */
    @Transactional
    public NoteDto.NoteInfoResp sendNote(NoteDto.NoteSendReq noteSendReq){
        Member member = memberRepository.findByLoginId(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new PojeException(ErrorCode.MEMBER_NOT_FOUND)
        );

        Member receiver = memberRepository.findByNickName(noteSendReq.getNickName()).orElseThrow(
                () -> new PojeException(ErrorCode.MEMBER_NOT_FOUND)
        );

        // 본인에게는 쪽지 불가
        if (member == receiver){
            throw new PojeException(ErrorCode.NOTE_SEND_ERROR);
        }

        // 쪽지 생성
        Note note = Note.builder()
                .sender(member)
                .receiver(receiver)
                .message(noteSendReq.getMessage())
                .build();

        noteRepository.save(note);

        return NoteDto.NoteInfoResp.builder()
                .note(note)
                .sendStatus(NoteStatus.SEND)
                .build();
    }
}
