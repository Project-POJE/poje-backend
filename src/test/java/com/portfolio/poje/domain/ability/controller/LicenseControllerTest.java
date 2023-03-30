package com.portfolio.poje.domain.ability.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.poje.config.jwt.JwtTokenProvider;
import com.portfolio.poje.domain.ability.dto.LicenseDto;
import com.portfolio.poje.domain.ability.service.LicenseService;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LicenseController.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@MockBean(JpaMetamodelMappingContext.class)
class LicenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper obj;

    @MockBean
    private LicenseService licenseService;

    @MockBean
    private JwtTokenProvider tokenProvider;


    private Collection<? extends GrantedAuthority> authorities(RoleType roleType){
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(roleType.name()));
        return authorities;
    }


    @Test
    @DisplayName("자격증 등록 테스트")
    @WithMockUser
    void createLicense() throws Exception{
        // given
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("test", "test", authorities(RoleType.ROLE_USER));
        given(tokenProvider.getAuthentication(anyString())).willReturn(authentication);
        doNothing().when(licenseService).enroll(any(LicenseDto.LicenseCreateReq.class));

        String content = obj.writeValueAsString(new LicenseDto.LicenseCreateReq("정보처리기사"));

        // when
        ResultActions result = mockMvc.perform(post("/member/license")
                        .with(csrf())
                        .header("Authorization", "Bearer + (accessToken)")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print());

        // then
        result.andExpect(status().isOk())
                .andDo(document("/license/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer + (access 토큰)")
                        ),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("자격증 명")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지")
                        )
                ));
    }


    @Test
    @DisplayName("자격증 수정 테스트")
    @WithMockUser
    void updateLicense() throws Exception{
        // given
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("test", "test", authorities(RoleType.ROLE_USER));
        given(tokenProvider.getAuthentication(anyString())).willReturn(authentication);

        List<LicenseDto.LicenseInfoResp> licenseInfoRespList = new ArrayList<>();
        licenseInfoRespList.add(new LicenseDto.LicenseInfoResp(1L, "정보처리 기능사"));
        licenseInfoRespList.add(new LicenseDto.LicenseInfoResp(2L, "리눅스 마스터"));

        given(licenseService.updateLicense(any(LicenseDto.LicenseUpdateReq.class))).willReturn(licenseInfoRespList);


        List<LicenseDto.LicenseInfoReq> licenseList = new ArrayList<>();
        licenseList.add(new LicenseDto.LicenseInfoReq("정보처리 기능사"));
        licenseList.add(new LicenseDto.LicenseInfoReq("리눅스 마스터"));

        LicenseDto.LicenseUpdateReq licenseUpdateReq = new LicenseDto.LicenseUpdateReq(licenseList);

        String content = obj.writeValueAsString(licenseUpdateReq);

        // when
        ResultActions result = mockMvc.perform(put("/member/license")
                        .with(csrf())
                        .header("Authorization", "Bearer + (accessToken)")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print());

        // then
        result.andExpect(status().isOk())
                .andDo(document("/license/update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer + (access 토큰)")
                        ),
                        requestFields(
                                fieldWithPath("licenseList.[].name").type(JsonFieldType.STRING).description("자격증 명")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지"),
                                fieldWithPath("result.[].licenseId").type(JsonFieldType.NUMBER).description("자격증 id"),
                                fieldWithPath("result.[].name").type(JsonFieldType.STRING).description("자격증 명")
                        )
                ));
    }


    @Test
    @DisplayName("자격증 목록 반환 테스트")
    @WithMockUser
    void licenseInfo() throws Exception{
        // given
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("test", "test", authorities(RoleType.ROLE_USER));
        given(tokenProvider.getAuthentication(anyString())).willReturn(authentication);

        List<LicenseDto.LicenseInfoResp> licenseInfoRespList = new ArrayList<>();
        licenseInfoRespList.add(new LicenseDto.LicenseInfoResp(1L, "정보처리 기능사"));
        licenseInfoRespList.add(new LicenseDto.LicenseInfoResp(2L, "리눅스 마스터"));

        given(licenseService.getLicenseList()).willReturn(licenseInfoRespList);

        // when
        ResultActions result = mockMvc.perform(get("/member/license")
                        .with(csrf())
                        .header("Authorization", "Bearer + (accessToken)")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        result.andExpect(status().isOk())
                .andDo(document("/license/list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer + (access 토큰)")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지"),
                                fieldWithPath("result.[].licenseId").type(JsonFieldType.NUMBER).description("자격증 id"),
                                fieldWithPath("result.[].name").type(JsonFieldType.STRING).description("자격증 명")
                        )
                ));
    }
}