package com.study.springbootstudy.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final long expirationTime;

    // application.yml의 jwt.secret과 jwt.access-expiration 값을 주입받음
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,
                            @Value("${jwt.access-expiration}") long expirationTime) {
        // Base64 문자열을 디코딩하여 암호화 알고리즘에 맞는 SecretKey 객체로 변환
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.expirationTime = expirationTime;
    }

    // 1. 토큰 생성 메서드 (로그인 성공 시 호출)
    public String createToken(String email) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .subject(email) // 토큰의 주체(subject)에 이메일 저장
                .issuedAt(now) // 발행 시간
                .expiration(validity) // 만료 시간
                .signWith(key) // SecretKey를 사용하여 서명
                .compact();
    }

    // 2. 토큰에서 이메일 추출 메서드 (인가 과정에서 호출)
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key) // 토큰 서명 검증
                .build()
                .parseSignedClaims(token)
                .getPayload(); // 페이로드(데이터) 추출
        return claims.getSubject(); // 저장해둔 이메일 반환
    }

    // 3. 토큰 유효성 검증 메서드
    public boolean validateToken(String token) {
        try {
            // 파싱 중 에러가 발생하지 않으면 유효한 토큰으로 간주
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            // 만료되었거나 변조된 토큰일 경우 false 반환
            return false;
        }
    }
}