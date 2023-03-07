package com.portfolio.poje.domain.ability.repository;

import com.portfolio.poje.domain.ability.entity.Note;
import com.portfolio.poje.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NoteRepository {

    private final EntityManager em;

    public void save(Note note) {
        em.persist(note);
    }

    public Optional<Note> findById(Long id) {
        return Optional.ofNullable(em.find(Note.class, id));
    }

    public void delete(Note note){
        em.remove(note);
    }


    /**
     * 쪽지 보낸 사람 목록
     * @param receiver
     * @return : List<Member>
     */
    public List<Member> findSenderByReceiver(Member receiver){
        return em.createQuery("select distinct n.sender from Note n where n.receiver = :receiver")
                .setParameter("receiver", receiver)
                .getResultList();
    }


    /**
     * 쪽지 받은 사람 목록
     * @param sender
     * @return : List<Member>
     */
    public List<Member> findReceiverBySender(Member sender){
        return em.createQuery("select distinct n.receiver from Note n where n.sender = :sender")
                .setParameter("sender", sender)
                .getResultList();
    }


    /**
     * 두 대상이 주고 받은 쪽지 중 가장 최근 쪽지 하나만 반환
     * @param sender
     * @param receiver
     * @return : Optional<Note>
     */
    public Optional<Note> findTop1BySenderAndReceiver(Member sender, Member receiver){
        return em.createQuery("select n from Note n where (n.sender = :sender and n.receiver = :receiver) or " +
                        "(n.sender = :receiver and n.receiver = :sender) order by n.createdDate desc")
                .setParameter("sender", sender)
                .setParameter("receiver", receiver)
                .getResultList()
                .stream().findFirst();
    }


}
