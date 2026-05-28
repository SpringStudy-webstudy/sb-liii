package com.study.springbootstudy.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. 요청 헤더에서 "Bearer [토큰]" 추출
        String token = resolveToken(request);

        // 2. 토큰이 존재하고 유효한지 검사
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 3. 토큰에서 이메일을 꺼내어 스프링 시큐리티의 인증 객체로 등록
            String email = jwtTokenProvider.getEmailFromToken(token);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());

            // 4. 전역 보안 컨텍스트에 현재 로그인한 사용자 정보 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 5. 검증이 끝나면 다음 필터나 컨트롤러로 요청을 넘김
        filterChain.doFilter(request, response);
    }

    // 헤더의 "Authorization" 키값에서 순수 토큰 문자열만 잘라내는 유틸리티 메서드
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}