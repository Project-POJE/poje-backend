package com.portfolio.poje.repository;

import com.portfolio.poje.domain.member.Member;
import com.portfolio.poje.domain.member.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

//@ActiveProfiles(value = "test")
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void 데이터준비(){
        Member member = Member.createMember()
                .loginId("test001")
                .password("test001~!")
                .nickName("tester001")
                .email("tester001@abc.com")
                .phoneNum("01012345678")
                .gender("male")
                .birth("23/01/18")
                .role(RoleType.ROLE_USER)
                .build();

        memberRepository.save(member);
    }

    @Test
    @DisplayName("Member save 테스트")
    void saveMember(){
        // given
        Member member = Member.createMember()
                .loginId("test001")
                .password("test001~!")
                .nickName("tester001")
                .email("tester001@abc.com")
                .phoneNum("01012345678")
                .gender("male")
                .birth("23/01/18")
                .role(RoleType.ROLE_USER)
                .build();

        // when
        Member savedMember = memberRepository.save(member);

        // then
        assertThat(member.getLoginId()).isEqualTo(savedMember.getLoginId());
        assertThat(member.getPassword()).isEqualTo(savedMember.getPassword());
        assertThat(member.getNickName()).isEqualTo(savedMember.getNickName());
        assertThat(member.getEmail()).isEqualTo(savedMember.getEmail());
    }

    @Test
    @DisplayName("Member findAll 테스트")
    void findMemberList(){
        // given
        Member member = Member.createMember()
                .loginId("test002")
                .password("test002~!")
                .nickName("tester002")
                .email("tester002@abc.com")
                .phoneNum("01012345678")
                .gender("male")
                .birth("23/01/18")
                .role(RoleType.ROLE_USER)
                .build();

        memberRepository.save(member);

        String loginId = "test001";
        String password = "test001~!";
        String nickName = "tester001";

        // when
        List<Member> members = memberRepository.findAll();

        // then
        assertThat(members.get(0).getLoginId()).isEqualTo(loginId);
        assertThat(members.get(0).getPassword()).isEqualTo(password);
        assertThat(members.get(0).getNickName()).isEqualTo(nickName);

        assertThat(members.get(1).getLoginId()).isEqualTo(member.getLoginId());
        assertThat(members.get(1).getPassword()).isEqualTo(member.getPassword());
        assertThat(members.get(1).getNickName()).isEqualTo(member.getNickName());
    }


    @Test
    @DisplayName("Member findByLoginId 테스트")
    void findMember(){
        // given
        String loginId = "test001";
        String password = "test001~!";
        String nickName = "tester001";

        // when
        Member findMember = memberRepository.findByLoginId(loginId).get();

        // then
        assertThat(loginId).isEqualTo(findMember.getLoginId());
        assertThat(password).isEqualTo(findMember.getPassword());
        assertThat(nickName).isEqualTo(findMember.getNickName());
    }


    @Test
    @DisplayName("Member existsByLoginId 테스트")
    void existsMember(){
        // given
        String loginId = "test001";

        // when
        boolean isPresent = memberRepository.existsByLoginId(loginId);

        // then
        assertThat(isPresent).isTrue();
    }


    @Test
    @DisplayName("Member update 테스트")
    void updateMember(){
        // given
        Long id = 1L;
        String loginId = "test007";
        String password = "password007~!";
        String nickName = "tester007";
        Member member = Member.createMember()
                .id(id)
                .loginId(loginId)
                .password(password)
                .nickName(nickName)
                .build();

        // when
        Member updatedMember = memberRepository.save(member);

        // then
        assertThat(id).isEqualTo(updatedMember.getId());
        assertThat(loginId).isEqualTo(updatedMember.getLoginId());
        assertThat(password).isEqualTo(updatedMember.getPassword());
        assertThat(nickName).isEqualTo(updatedMember.getNickName());
    }


    @Test
    @DisplayName("Member deleteById 테스트")
    void deleteMember(){
        // given
        Long id = 1L;

        // when
        memberRepository.deleteById(id);

        // then
        assertFalse(memberRepository.findById(id).isPresent());
    }

}