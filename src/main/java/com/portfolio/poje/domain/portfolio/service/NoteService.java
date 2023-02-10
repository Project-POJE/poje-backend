package com.portfolio.poje.domain.portfolio.service;

import com.portfolio.poje.common.exception.ErrorCode;
import com.portfolio.poje.common.exception.PojeException;
import com.portfolio.poje.config.SecurityUtil;
import com.portfolio.poje.domain.member.entity.Member;
import com.portfolio.poje.domain.member.repository.MemberRepository;
import com.portfolio.poje.domain.portfolio.dto.noteDto.NoteInfoResp;
import com.portfolio.poje.domain.portfolio.dto.noteDto.NoteSendReq;
import com.portfolio.poje.domain.portfolio.dto.noteDto.PfWithNoteInfoResp;
import com.portfolio.poje.domain.portfolio.entity.Note;
import com.portfolio.poje.domain.portfolio.entity.NoteStatus;
import com.portfolio.poje.domain.portfolio.entity.Portfolio;
import com.portfolio.poje.domain.portfolio.repository.NoteRepository;
import com.portfolio.poje.domain.portfolio.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class NoteService {

    private final MemberRepository memberRepository;
    private final PortfolioRepository portfolioRepository;
    private final NoteRepository noteRepository;


    /**
     * 포트폴리오 작성자에게 쪽지 전송
     * @param portfolioId
     * @param noteSendReq
     * @return : NoteInfoResp
     */
    @Transactional
    public NoteInfoResp sendNote(Long portfolioId, NoteSendReq noteSendReq){
        Member member = memberRepository.findByLoginId(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new PojeException(ErrorCode.MEMBER_NOT_FOUND)
        );

        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow(
                () -> new PojeException(ErrorCode.PORTFOLIO_NOT_FOUND)
        );

        // 본인에게는 쪽지 불가
        if (member == portfolio.getWriter()){
            throw new PojeException(ErrorCode.NOTE_SEND_ERROR);
        }

        Note note = Note.builder()
                .sender(member)
                .receiver(portfolio.getWriter())
                .message(noteSendReq.getMessage())
                .portfolio(portfolio)
                .build();

        noteRepository.save(note);

        return NoteInfoResp.builder()
                .note(note)
                .sendStatus(NoteStatus.SEND)
                .build();
    }


    /**
     * 쪽지 보낸 사람한테 답장하기
     * @param noteId
     * @param noteSendReq
     * @return : NoteInfoResp
     */
    @Transactional
    public NoteInfoResp replyNote(Long noteId, NoteSendReq noteSendReq){
        Member member = memberRepository.findByLoginId(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new PojeException(ErrorCode.MEMBER_NOT_FOUND)
        );

        Note receivedNote = noteRepository.findById(noteId).orElseThrow(
                () -> new PojeException(ErrorCode.NOTE_NOT_FOUND)
        );

        if (member != receivedNote.getReceiver()){
            throw new PojeException(ErrorCode.NOTE_SEND_ERROR);
        }

        Note note = Note.builder()
                .sender(member)
                .receiver(receivedNote.getSender())
                .message(noteSendReq.getMessage())
                .portfolio(receivedNote.getPortfolio())
                .build();

        noteRepository.save(note);

        return NoteInfoResp.builder()
                .note(note)
                .sendStatus(NoteStatus.SEND)
                .build();
    }


    /**
     * 쪽지 정보를 가진 포트폴리오 목록 반환
     * @return : List<PfWithNoteInfoResp>
     */
    @Transactional(readOnly = true)
    public List<PfWithNoteInfoResp> getPortfolioListWithNote(){
        Member member = memberRepository.findByLoginId(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new PojeException(ErrorCode.MEMBER_NOT_FOUND)
        );

        List<Note> noteList = noteRepository.findByMember(member);

        Set<Portfolio> portfolioList = noteList.stream()
                .map(note -> note.getPortfolio())
                .collect(Collectors.toSet());

        return portfolioList.stream()
                .map(portfolio -> new PfWithNoteInfoResp(portfolio.getId(), portfolio.getTitle()))
                .collect(Collectors.toList());
    }


    /**
     * 포트폴리오에서 송수신한 쪽지 목록 반환
     * @param portfolioId
     * @return : List<NoteInfoResp>
     */
    @Transactional(readOnly = true)
    public List<NoteInfoResp> getNoteInfoList(Long portfolioId){
        Member member = memberRepository.findByLoginId(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new PojeException(ErrorCode.MEMBER_NOT_FOUND)
        );

        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow(
                () -> new PojeException(ErrorCode.PORTFOLIO_NOT_FOUND)
        );

        List<Note> noteList = noteRepository.findByMemberAndPortfolio(member, portfolio);

        return noteList.stream()
                .map(note -> {
                    if (note.getSender() == member){
                        return NoteInfoResp.builder()
                                .note(note)
                                .sendStatus(NoteStatus.SEND)
                                .build();
                    }
                    return NoteInfoResp.builder()
                            .note(note)
                            .sendStatus(NoteStatus.RECEIVE)
                            .build();
                }).collect(Collectors.toList());
    }


}
