package com.study.springbootstudy.dto;

import com.study.springbootstudy.domain.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;

    // DB에서 꺼낸 Post(Entity)를 DTO로 변환해 주는 메서드
    public static PostResponseDto from(Post post) {
        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }
}