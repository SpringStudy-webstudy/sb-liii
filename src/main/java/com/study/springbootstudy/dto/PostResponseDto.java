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
    private String authorName;

    // DB에서 꺼낸 Post(Entity)를 DTO로 변환해 주는 메서드
    public static PostResponseDto from(Post post) {
        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .authorName(post.getMember().getName())
                // 바로 이 부분에서 Member 객체에 접근해서 이름을 꺼내올 때 N+1 문제가 발생하기 쉬운데,
                // Repository에서 JOIN FETCH를 썼기 때문에 아주 안전하고 빠르게 가져온다.
                .build();
    }
}