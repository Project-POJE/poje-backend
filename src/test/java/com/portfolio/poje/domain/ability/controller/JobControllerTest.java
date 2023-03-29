package com.portfolio.poje.domain.ability.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.poje.config.jwt.JwtTokenProvider;
import com.portfolio.poje.domain.ability.dto.JobDto;
import com.portfolio.poje.domain.ability.service.JobService;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
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
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JobController.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@MockBean(JpaMetamodelMappingContext.class)
class JobControllerTest {

    @Autowired
    private MockMvc mockMvc; // mockMvc 생성

    @Autowired
    private ObjectMapper obj;

    @MockBean
    private JobService jobService;

    @MockBean
    private JwtTokenProvider tokenProvider;


    private Collection<? extends GrantedAuthority> authorities(RoleType roleType){
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(roleType.name()));
        return authorities;
    }

    @Test
    @DisplayName("직무 등록 테스트")
    @WithMockUser
    void enroll() throws Exception {
        // given
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("admin", "admin", authorities(RoleType.ROLE_ADMIN));
        given(tokenProvider.getAuthentication(anyString())).willReturn(authentication);
        doNothing().when(jobService).createJob(any(JobDto.JobCreateReq.class));

        String content = obj.writeValueAsString(new JobDto.JobCreateReq("개발자"));

        // when
        ResultActions result = mockMvc.perform(post("/admin/job")
                .with(csrf())
                .header("Authorization", "Bearer (accessToken)")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print());

        // then
        result.andExpect(status().isOk())
                .andDo(document("/job/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer + (관리자 access 토큰)")
                        ),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("직무 명")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("직무 목록 반환 테스트")
    @WithMockUser
    void getJobList() throws Exception {
        // given
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("test", "test", authorities(RoleType.ROLE_USER));
        given(tokenProvider.getAuthentication(anyString())).willReturn(authentication);

        List<String> jobList = new ArrayList<>();
        jobList.add("개발자");
        jobList.add("디자이너");

        JobDto.JobListResp jobListResp = new JobDto.JobListResp(jobList);
        given(jobService.getJobList()).willReturn(jobListResp);

        // when
        ResultActions result = mockMvc.perform(get("/job")
                .with(csrf())
                .header("Authorization", "Bearer (accessToken)")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        result.andExpect(status().isOk())
                .andDo(document("/job/list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer + (access 토큰)")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지"),
                                fieldWithPath("result.jobInfoRespList.[].name").type(JsonFieldType.STRING).description("반환할 직무 명")
                        )
                ));
    }

    @Test
    @DisplayName("직무 명 수정 테스트")
    @WithMockUser
    void updateJobInfo() throws Exception{
        // given
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("admin", "admin", authorities(RoleType.ROLE_ADMIN));
        given(tokenProvider.getAuthentication(anyString())).willReturn(authentication);

        List<String> jobList = new ArrayList<>();
        jobList.add("개발자");
        jobList.add("디자이너");

        JobDto.JobListResp jobListResp = new JobDto.JobListResp(jobList);
        given(jobService.updateJobInfo(anyLong(), any(JobDto.JobUpdateReq.class))).willReturn(jobListResp);

        String content = obj.writeValueAsString(new JobDto.JobUpdateReq("개발자(수정)"));

        // when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.put("/admin/job/{job_id}", 1L)
                .with(csrf())
                .header("Authorization", "Bearer (accessToken)")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print());

        // then
        result.andExpect(status().isOk())
                .andDo(document("/job/update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("job_id").description("직무 id")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer + (관리자 access 토큰)")
                        ),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("직무 명")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지"),
                                fieldWithPath("result.jobInfoRespList.[].name").type(JsonFieldType.STRING).description("반환할 직무 명")
                        )
                ));
    }

    @Test
    @DisplayName("직무 삭제 테스트")
    @WithMockUser
    void deleteJobInfo() throws Exception{
        // given
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("admin", "admin", authorities(RoleType.ROLE_ADMIN));
        given(tokenProvider.getAuthentication(anyString())).willReturn(authentication);
        doNothing().when(jobService).deleteJobInfo(anyLong());

        // when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.delete("/admin/job/{job_id}", 1L)
                .with(csrf())
                .header("Authorization", "Bearer + (accessToken)")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        result.andExpect(status().isOk())
                .andDo(document("/job/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("job_id").description("직무 id")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer + (관리자 access 토큰)")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지")
                        )
                ));
    }
}