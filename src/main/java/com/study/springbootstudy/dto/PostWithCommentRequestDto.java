package com.study.springbootstudy.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostWithCommentRequestDto {

    @NotBlank(message = "게시글 제목은 필수입니다.")
    private String title;

    @NotBlank(message = "게시글 내용은 필수입니다.")
    private String content;

    @NotBlank(message = "초기 댓글 내용은 필수입니다.")
    private String commentContent;
}