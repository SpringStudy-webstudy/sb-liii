package com.study.springbootstudy.controller;

import com.study.springbootstudy.common.ApiResponse;
import com.study.springbootstudy.dto.MemberJoinRequestDto;
import com.study.springbootstudy.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.study.springbootstudy.dto.MemberLoginRequestDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")

public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ApiResponse<Long> join(@RequestBody @Valid MemberJoinRequestDto request) {
        Long memberId = memberService.join(request);
        return ApiResponse.onSuccess(memberId); // 기존에 작성하신 ApiResponse 규격에 맞게 호출
    }

    @PostMapping("/login")
    public ApiResponse<String> login(@RequestBody @Valid MemberLoginRequestDto request) {
        // 서비스에서 생성된 토큰 문자열을 그대로 반환받음
        String token = memberService.login(request);

        // 추가 DTO 없이 토큰 문자열 자체를 result에 담아 응답
        return ApiResponse.onSuccess(token);
    }
}