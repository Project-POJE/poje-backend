package com.portfolio.poje.domain.ability.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.poje.config.jwt.JwtTokenProvider;
import com.portfolio.poje.domain.ability.dto.NoteDto;
import com.portfolio.poje.domain.ability.entity.Note;
import com.portfolio.poje.domain.ability.entity.NoteStatus;
import com.portfolio.poje.domain.ability.service.NoteService;
import com.portfolio.poje.domain.member.entity.Member;
import com.portfolio.poje.domain.member.entity.RoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NoteController.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@MockBean(JpaMetamodelMappingContext.class)
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper obj;

    @MockBean
    private NoteService noteService;

    @MockBean
    private JwtTokenProvider tokenProvider;


    private Collection<? extends GrantedAuthority> authorities(RoleType roleType){
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(roleType.name()));
        return authorities;
    }


    @Test
    @DisplayName("쪽지 보내기 테스트")
    @WithMockUser
    void sendNote() throws Exception{
        // given
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("test", "test", authorities(RoleType.ROLE_USER));
        given(tokenProvider.getAuthentication(anyString())).willReturn(authentication);

        Member sender = Member.createMember()
                .nickName("sender nickName")
                .email("sender email")
                .build();

        Member receiver = Member.createMember()
                .nickName("receiver nickName")
                .email("receiver email")
                .build();

        Note note = Note.builder()
                .sender(sender)
                .receiver(receiver)
                .message("test message")
                .build();

        NoteDto.NoteInfoResp noteInfoResp = NoteDto.NoteInfoResp.builder()
                .note(note)
                .sendStatus(NoteStatus.SEND)
                .build();

        given(noteService.sendNote(any(NoteDto.NoteSendReq.class))).willReturn(noteInfoResp);

        NoteDto.NoteSendReq sendReq = new NoteDto.NoteSendReq("test@test.com", "test message");
        String content = obj.writeValueAsString(sendReq);

        // when
        ResultActions result = mockMvc.perform(post("/member/note")
                .with(csrf())
                .header("Authorization", "Bearer + (accessToken)")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print());

        // then
        result.andExpect(status().isOk())
                .andDo(document("/note/send",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer + (access 토큰)")
                        ),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("수신자 이메일"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("보낼 메시지")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지"),
                                fieldWithPath("result.message").type(JsonFieldType.STRING).description("메시지"),
                                fieldWithPath("result.senderNickName").type(JsonFieldType.STRING).description("송신자 닉네임"),
                                fieldWithPath("result.senderEmail").type(JsonFieldType.STRING).description("송신자 이메일"),
                                fieldWithPath("result.sendStatus").type(JsonFieldType.STRING).description("송수신 여부"),
                                fieldWithPath("result.sendTime").type(JsonFieldType.STRING).description("보낸 시간").ignored()
                        )
                ));
    }


    @Test
    @DisplayName("쪽지함 조회 테스트")
    @WithMockUser
    void enterNoteRoom() throws Exception{
        // given
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("test", "test", authorities(RoleType.ROLE_USER));
        given(tokenProvider.getAuthentication(anyString())).willReturn(authentication);

        List<NoteDto.RecentNoteResp> recentNoteRespList = new ArrayList<>();
        recentNoteRespList.add(NoteDto.RecentNoteResp.builder()
                .opponentNickName("tester")
                .opponentEmail("test@test.com")
                .lastMessage("test message")
                .sendTime(LocalDateTime.now())
                .isView(true)
                .build());

        given(noteService.enterNoteRoom()).willReturn(recentNoteRespList);

        // when
        ResultActions result = mockMvc.perform(get("/member/note-room")
                        .with(csrf())
                        .header("Authorization", "Bearer + (accessToken)")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        result.andExpect(status().isOk())
                .andDo(document("/note/enter",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer + (access 토큰)")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지"),
                                fieldWithPath("result.[].opponentNickName").type(JsonFieldType.STRING).description("상대 닉네임"),
                                fieldWithPath("result.[].opponentEmail").type(JsonFieldType.STRING).description("상대 이메일"),
                                fieldWithPath("result.[].lastMessage").type(JsonFieldType.STRING).description("최근 메시지"),
                                fieldWithPath("result.[].sendTime").type(JsonFieldType.STRING).description("보낸 시간"),
                                fieldWithPath("result.[].view").type(JsonFieldType.BOOLEAN).description("쪽지 봤는지 여부")
                        )
                ));
    }


    @Test
    @DisplayName("족지 가져오기 테스트")
    @WithMockUser
    void getNotes() throws Exception{
        // given
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("test", "test", authorities(RoleType.ROLE_USER));
        given(tokenProvider.getAuthentication(anyString())).willReturn(authentication);

        Member sender = Member.createMember()
                .nickName("sender nickName")
                .email("sender email")
                .build();

        Member receiver = Member.createMember()
                .nickName("receiver nickName")
                .email("receiver email")
                .build();

        Note note = Note.builder()
                .sender(sender)
                .receiver(receiver)
                .message("test message")
                .build();

        List<NoteDto.NoteInfoResp> noteInfoRespList = new ArrayList<>();
        noteInfoRespList.add(NoteDto.NoteInfoResp.builder()
                .note(note)
                .sendStatus(NoteStatus.SEND)
                .build());

        given(noteService.getNotes(anyString())).willReturn(noteInfoRespList);

        // when
        ResultActions result = mockMvc.perform(get("/member/note")
                .header("Authorization", "Bearer + (accessToken)")
                .contentType(MediaType.APPLICATION_JSON)
                .param("email", "test@test.com"))
                .andDo(print());

        // then
        result.andExpect(status().isOk())
                .andDo(document("/note/list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer + (access 토큰)")
                        ),
                        requestParameters(
                                parameterWithName("email").description("대상 닉네임")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지"),
                                fieldWithPath("result.[].message").type(JsonFieldType.STRING).description("메시지"),
                                fieldWithPath("result.[].senderNickName").type(JsonFieldType.STRING).description("송신자 닉네임"),
                                fieldWithPath("result.[].senderEmail").type(JsonFieldType.STRING).description("송신자 이메일"),
                                fieldWithPath("result.[].sendStatus").type(JsonFieldType.STRING).description("송수신 여부"),
                                fieldWithPath("result.[].sendTime").type(JsonFieldType.STRING).description("보낸 시간").ignored()
                        )
                ));
    }


    @Test
    @DisplayName("안 본 쪽지 여부 테스트")
    @WithMockUser
    void getNoteAlarm() throws Exception{
        // given
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("test", "test", authorities(RoleType.ROLE_USER));
        given(tokenProvider.getAuthentication(anyString())).willReturn(authentication);

        NoteDto.NoteAlarmResp noteAlarmResp = new NoteDto.NoteAlarmResp(false);
        given(noteService.getNoteAlarm()).willReturn(noteAlarmResp);

        // when
        ResultActions result = mockMvc.perform(get("/member/note/alarm")
                        .with(csrf())
                        .header("Authorization", "Bearer + (accessToken)")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        result.andExpect(status().isOk())
                .andDo(document("/note/alarm",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer + (access 토큰)")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지"),
                                fieldWithPath("result.exists").type(JsonFieldType.BOOLEAN).description("쪽지 봤는지 여부")
                        )
                ));
    }
}