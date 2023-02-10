package com.portfolio.poje.domain.portfolio.controller;

import com.portfolio.poje.common.BasicResponse;
import com.portfolio.poje.domain.portfolio.dto.noteDto.NoteInfoResp;
import com.portfolio.poje.domain.portfolio.dto.noteDto.NoteSendReq;
import com.portfolio.poje.domain.portfolio.dto.noteDto.PfWithNoteInfoResp;
import com.portfolio.poje.domain.portfolio.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class NoteController {

    private final NoteService noteService;


    /**
     * 포트폴리오 작성자에게 쪽지 전송
     * @param portfolioId
     * @param noteSendReq
     * @return : NoteInfoResp
     */
    @PostMapping("/portfolio/{portfolio_id}/note")
    public ResponseEntity<BasicResponse> sendNote(@PathVariable(value = "portfolio_id") Long portfolioId,
                                                  @RequestBody NoteSendReq noteSendReq){
        NoteInfoResp noteInfoResp = noteService.sendNote(portfolioId, noteSendReq);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "쪽지가 전송되었습니다.", noteInfoResp));
    }


    /**
     * 쪽지 보낸 사람한테 답장
     * @param noteId
     * @param noteSendReq
     * @return : NoteInfoResp
     */
    @PostMapping("/note/{note_id}")
    public ResponseEntity<BasicResponse> replyNote(@PathVariable(value = "note_id") Long noteId,
                                                   @RequestBody NoteSendReq noteSendReq){
        NoteInfoResp noteInfoResp = noteService.replyNote(noteId, noteSendReq);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "답장이 전송되었습니다.", noteInfoResp));
    }


    /**
     * 쪽지 정보를 가진 포트폴리오 목록 반환
     * @return : List<PfWithNoteInfoResp>
     */
    @GetMapping("/note/portfolios")
    public ResponseEntity<BasicResponse> getPortfolioListWithNote(){
        List<PfWithNoteInfoResp> pfWithNoteInfoRespList = noteService.getPortfolioListWithNote();

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "쪽지 정보가 있는 포트폴리오 목록 반환", pfWithNoteInfoRespList));
    }


    /**
     * 포트폴리오에서 송수신한 쪽지 목록 반환
     * @param portfolioId
     * @return : List<NoteInfoResp>
     */
    @GetMapping("/portfolio/{portfolio_id}/note")
    public ResponseEntity<BasicResponse> getNoteInfo(@PathVariable(value = "portfolio_id") Long portfolioId){
        List<NoteInfoResp> noteInfoRespList = noteService.getNoteInfoList(portfolioId);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "쪽지 정보 반환", noteInfoRespList));
    }


}
