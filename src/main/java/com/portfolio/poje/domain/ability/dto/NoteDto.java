package com.portfolio.poje.domain.ability.dto;

import com.portfolio.poje.domain.ability.entity.Note;
import com.portfolio.poje.domain.ability.entity.NoteStatus;
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

        private String nickName;

        private String message;
    }


    /**
     * 쪽지 정보 응답 Dto
     */
    @Getter
    public static class NoteInfoResp {

        private String message;

        private String sender;

        private NoteStatus sendStatus;

        private LocalDateTime sendTime;

        @Builder
        private NoteInfoResp(Note note, NoteStatus sendStatus){
            this.message = note.getMessage();
            this.sender = note.getSender().getNickName();
            this.sendStatus = sendStatus;
            this.sendTime = note.getCreatedDate();
        }
    }


    /**
     * 최근에 주고 받은 쪽지 정보 응답 Dto
     */
    @Getter
    @Builder
    public static class RecentNoteResp {

        private String opponentNickName;

        private String lastMessage;

        private LocalDateTime sendTime;

        private boolean isView;
    }

}
