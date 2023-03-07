package com.portfolio.poje.domain.ability.entity;

import com.portfolio.poje.common.BaseEntity;
import com.portfolio.poje.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Note extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "note_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Member receiver;

    private String message;

    private boolean view;


    @Builder
    private Note(Member sender, Member receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.view = false;
    }

    public void viewNote(){
        this.view = true;
    }

}
