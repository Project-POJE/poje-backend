package com.portfolio.poje.config.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // access token 추출
        String token = resolveToken((HttpServletRequest) request);

        // 토큰이 유효한지 확인
        if (token != null && jwtTokenProvider.validateToken(token)){
            // Redis에 해당 accessToken logout 여부를 확인
            String isLogout = redisTemplate.opsForValue().get(token);

            // 로그아웃된 토큰이 아니면 정상 처리
            if (ObjectUtils.isEmpty(isLogout)) {
                // 토큰에서 Authentication 객체를 가져와서 SecurityContext에 저장
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        }
        chain.doFilter(request, response);
    }


    /**
     * Request Header에서 JWT 토큰 추출
     * @param request
     * @return : Jwt 토큰
     */
    private String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")){
            return bearerToken.substring(7);
        }

        return null;
    }

}