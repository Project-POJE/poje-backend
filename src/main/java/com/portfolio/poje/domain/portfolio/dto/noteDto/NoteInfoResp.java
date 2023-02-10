package com.portfolio.poje.domain.portfolio.dto.noteDto;

import com.portfolio.poje.domain.portfolio.entity.Note;
import com.portfolio.poje.domain.portfolio.entity.NoteStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NoteInfoResp {

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
