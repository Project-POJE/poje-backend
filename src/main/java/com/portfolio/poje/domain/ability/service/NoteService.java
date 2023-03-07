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

import java.util.*;
import java.util.stream.Collectors;

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


    /**
     * 쪽지함으로 이동
     * @return : List<RecentNoteResp>
     */
    @Transactional(readOnly = true)
    public List<NoteDto.RecentNoteResp> enterNoteRoom(){
        Member member = memberRepository.findByLoginId(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new PojeException(ErrorCode.MEMBER_NOT_FOUND)
        );

        // 사용자에게 쪽지 보낸 사람
        List<Member> allWriterList = noteRepository.findSenderByReceiver(member);
        // 사용자에게 쪽지 받은 사람
        allWriterList.addAll(noteRepository.findReceiverBySender(member));

        // 쪽지 주고 받은 사람 중복 제거
        Set<Member> writerSet = new HashSet<>();
        for (Member writer: allWriterList){
            writerSet.add(writer);
        }

        // 상대방들과 최근에 주고 받은 쪽지 정보 반환할 Dto
        List<NoteDto.RecentNoteResp> recentNoteRespList = new ArrayList<>();

        // writerSet 출력
        Iterator<Member> it = writerSet.iterator();
        while(it.hasNext()){
            Member opponent = it.next();

            // 가장 최근에 주고 받은 쪽지 정보
            Note note = noteRepository.findTop1BySenderAndReceiver(opponent, member).orElseThrow(
                    () -> new PojeException(ErrorCode.NOTE_NOT_FOUND)
            );

            // 작성자가 본인이면 봤다고 표시
            boolean flag = true;
            // 본인이 아니면 안봤다고 표시
            if (member != note.getSender()) {
                flag = note.isView();
            }

            recentNoteRespList.add(NoteDto.RecentNoteResp.builder()
                    .opponentNickName(opponent.getNickName())
                    .lastMessage(note.getMessage())
                    .sendTime(note.getCreatedDate())
                    .isView(flag)
                    .build());
        }

        return recentNoteRespList;
    }


    /**
     * 상대방과 송수신한 쪽지 목록 반환
     * @param nickName
     * @return : List<NoteInfoResp>
     */
    @Transactional
    public List<NoteDto.NoteInfoResp> getNotes(String nickName){
        Member member = memberRepository.findByLoginId(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new PojeException(ErrorCode.MEMBER_NOT_FOUND)
        );

        Member opponent = memberRepository.findByNickName(nickName).orElseThrow(
                () -> new PojeException(ErrorCode.MEMBER_NOT_FOUND)
        );

        // opponent와 송수신한 쪽지 목록
        List<Note> noteList = noteRepository.findBySenderAndReceiver(member, opponent);

        return noteList.stream()
                .map(note -> {
                    if (note.getSender() == member){
                        return NoteDto.NoteInfoResp.builder()
                                .note(note)
                                .sendStatus(NoteStatus.SEND)
                                .build();
                    }

                    // 봤다고 표시
                    note.viewNote();
                    noteRepository.save(note);

                    return NoteDto.NoteInfoResp.builder()
                            .note(note)
                            .sendStatus(NoteStatus.RECEIVE)
                            .build();
                }).collect(Collectors.toList());
    }


    /**
     * 안 본 쪽지 존재 여부 확인
     * @return : NoteAlarmResp
     */
    @Transactional(readOnly = true)
    public NoteDto.NoteAlarmResp getNoteAlarm(){
        Member member = memberRepository.findByLoginId(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new PojeException(ErrorCode.MEMBER_NOT_FOUND)
        );

        boolean flag = false;
        if (noteRepository.findNoteSeenYet(member).isPresent()){
            flag = true;
        }

        return new NoteDto.NoteAlarmResp(flag);
    }


}
