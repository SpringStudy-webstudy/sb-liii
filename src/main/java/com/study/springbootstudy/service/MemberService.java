package com.study.springbootstudy.service;

import com.study.springbootstudy.common.exception.GeneralException;
import com.study.springbootstudy.domain.Member;
import com.study.springbootstudy.dto.MemberJoinRequestDto;
import com.study.springbootstudy.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.study.springbootstudy.security.JwtTokenProvider;
import com.study.springbootstudy.dto.MemberLoginRequestDto;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder; // SecurityConfig에서 등록한 빈 주입
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public Long join(MemberJoinRequestDto request) {
        // 1. 이메일 중복 검증
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new GeneralException("MEMBER409", "이미 존재하는 이메일입니다.");
        }

        // 2. 비밀번호 암호화 및 엔티티 생성
        Member member = Member.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // BCrypt 암호화 적용
                .name(request.getName())
                .build();

        // 3. DB 영속화
        return memberRepository.save(member).getId();
    }

    public String login(MemberLoginRequestDto request) {
        // 1. 이메일로 회원 조회
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new GeneralException("MEMBER404", "가입되지 않은 이메일입니다."));

        // 2. 비밀번호 검증 (입력받은 평문 vs DB에 저장된 암호화 문자열)
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new GeneralException("AUTH401", "비밀번호가 일치하지 않습니다.");
        }

        // 3. 인증 성공 시 JWT 토큰 생성 및 반환
        return jwtTokenProvider.createToken(member.getEmail());
    }
}