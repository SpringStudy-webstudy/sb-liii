package com.study.springbootstudy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PostRequestDto {
    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @NotBlank(message = "내용은 필수입니다.")
    @Size(min = 10, message = "내용은 최소 10자 이상이어야 합니다.")
    private String content;

    // 추가: 클라이언트로부터 비밀번호도 입력받음
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
}