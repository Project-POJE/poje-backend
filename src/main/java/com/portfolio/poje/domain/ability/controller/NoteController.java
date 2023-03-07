package com.portfolio.poje.domain.ability.controller;

import com.portfolio.poje.common.BasicResponse;
import com.portfolio.poje.domain.ability.dto.NoteDto;
import com.portfolio.poje.domain.ability.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class NoteController {

    private final NoteService noteService;


    /**
     * 닉네임으로 쪽지 전송
     * @param noteSendReq
     * @return : NoteInfoResp
     */
    @PostMapping("/note")
    public ResponseEntity<BasicResponse> sendNote(@RequestBody NoteDto.NoteSendReq noteSendReq){
        NoteDto.NoteInfoResp noteInfoResp = noteService.sendNote(noteSendReq);

        return ResponseEntity.ok(new BasicResponse(HttpStatus.OK.value(), "쪽지가 전송되었습니다.", noteInfoResp));
    }
}
