package com.portfolio.poje.domain.portfolio.dto;

import com.portfolio.poje.domain.portfolio.entity.Note;
import com.portfolio.poje.domain.portfolio.entity.NoteStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class NoteDto {

    /**
     * 쪽지 전송 요청 Dto
     */
    @Getter
    @NoArgsConstructor
    public static class NoteSendReq {

        private String message;
    }


    /**
     * 쪽지 정보 응답 Dto
     */
    @Getter
    public static class NoteInfoResp {

        private Long id;

        private String message;

        private NoteStatus sendStatus;

        private LocalDateTime sendTime;

        @Builder
        private NoteInfoResp(Note note, NoteStatus sendStatus){
            this.id = note.getId();
            this.message = note.getMessage();
            this.sendStatus = sendStatus;
            this.sendTime = note.getCreatedDate();
        }
    }


    /**
     * 쪽지 정보를 가진 포트폴리오 정보 응답 Dto
     */
    @Getter
    @AllArgsConstructor
    public static class PfWithNoteInfoResp {

        private Long portfolioId;

        private String title;
    }

}
