package com.study.springbootstudy.dto;

import com.study.springbootstudy.domain.Post;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class PostPageResponseDto {
    private List<PostResponseDto> postList; // 게시글 목록
    private int totalPages;                 // 전체 페이지 수
    private long totalElements;             // 전체 게시글 수
    private boolean isFirst;                // 첫 번째 페이지 여부
    private boolean isLast;                 // 마지막 페이지 여부

    // Page<Post> 객체를 받아서 DTO로 변환해 주는 팩토리 메서드
    public static PostPageResponseDto from(Page<Post> postPage) {
        return PostPageResponseDto.builder()
                // Page 안의 Post 엔티티들을 PostResponseDto로 변환
                .postList(postPage.getContent().stream()
                        .map(PostResponseDto::from)
                        .collect(Collectors.toList()))
                .totalPages(postPage.getTotalPages())
                .totalElements(postPage.getTotalElements())
                .isFirst(postPage.isFirst())
                .isLast(postPage.isLast())
                .build();
    }
}