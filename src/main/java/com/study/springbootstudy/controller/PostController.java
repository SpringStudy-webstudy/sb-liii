package com.study.springbootstudy.controller;

import com.study.springbootstudy.common.ApiResponse;
import com.study.springbootstudy.dto.PostRequestDto;
import com.study.springbootstudy.dto.PostResponseDto;
import com.study.springbootstudy.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.study.springbootstudy.dto.PostPageResponseDto;
import com.study.springbootstudy.dto.PostWithCommentRequestDto;

import java.security.Principal; // 👈 시큐리티가 제공하는 인증 정보 객체

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    // [목록 조회] 페이징 처리된 게시글 목록 반환 (누구나 볼 수 있으므로 토큰 검증 안 함)
    @GetMapping
    public ApiResponse<PostPageResponseDto> getPostList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        // 페이지 번호가 1보다 작으면 1로 강제 고정 (오류 방지)
        int validatedPage = Math.max(1, page);

        PostPageResponseDto response = postService.getPostList(validatedPage, size);
        return ApiResponse.onSuccess(response);
    }

    // [생성] Principal 객체를 통해 현재 로그인한 사용자의 이메일을 가져옴
    @PostMapping
    public ApiResponse<Long> createPost(@RequestBody @Valid PostRequestDto request, Principal principal) {
        // principal.getName()을 호출하면, 필터에서 토큰을 쪼개 저장했던 '이메일'이 나온다
        Long postId = postService.createPost(request, principal.getName());
        return ApiResponse.onSuccess(postId);
    }

    // [조회] 단건 조회 (조회는 권한 검증 없이 누구나 볼 수 있도록 둔다.)
    @GetMapping("/{postId}")
    public ApiResponse<PostResponseDto> getPost(@PathVariable Long postId) {
        PostResponseDto response = postService.getPostDetail(postId);
        return ApiResponse.onSuccess(response);
    }

    // [수정] URL 경로의 ID, 수정할 데이터, 그리고 로그인한 사용자(Principal)를 받는다.
    @PutMapping("/{id}")
    public ApiResponse<PostResponseDto> updatePost(@PathVariable Long id,
                                                   @RequestBody @Valid PostRequestDto request,
                                                   Principal principal) {
        PostResponseDto response = postService.updatePost(id, request, principal.getName());
        return ApiResponse.onSuccess(response);
    }

    // [삭제] 기존의 @RequestParam String password를 삭제하고 Principal로 교체한다.
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePost(@PathVariable Long id, Principal principal) {
        postService.deletePost(id, principal.getName());
        return ApiResponse.onSuccess(null); // 삭제는 돌려줄 데이터가 없으므로 null 반환
    }

    // 게시글 + 댓글 동시 저장
    @PostMapping("/with-comment")
    public ApiResponse<Long> createPostWithComment(
            @RequestBody @Valid PostWithCommentRequestDto request,
            Principal principal) {

        // principal.getName()을 통해 토큰을 통과한 유저의 이메일을 Service로 전달
        Long postId = postService.createPostWithComment(request, principal.getName());
        return ApiResponse.onSuccess(postId);
    }
}