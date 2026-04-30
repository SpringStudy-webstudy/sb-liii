package com.study.springbootstudy.controller;

import com.study.springbootstudy.common.ApiResponse;
import com.study.springbootstudy.dto.PostRequestDto;
import com.study.springbootstudy.dto.PostResponseDto;
import com.study.springbootstudy.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor // Service를 주입받기 위해 꼭 필요
@RequestMapping("/api/posts") // 이 컨트롤러의 기본 주소를 설정
public class PostController {

    private final PostService postService;

    // 1. 게시글 생성 API
    @PostMapping
    public ApiResponse<Long> createPost(@Valid @RequestBody PostRequestDto request) {
        Long savedPostId = postService.createPost(request);
        return ApiResponse.onSuccess(savedPostId); // 성공 시 저장된 게시글의 ID를 반환
    }

    // 2. 게시글 상세 조회 API
    @GetMapping("/{postId}")
    public ApiResponse<PostResponseDto> getPost(@PathVariable Long postId) {
        PostResponseDto response = postService.getPostDetail(postId);
        return ApiResponse.onSuccess(response); // 성공 시 게시글 상세 정보를 반환
    }

    // 게시글 수정 (PUT)
    @PutMapping("/{id}")
    public ApiResponse<PostResponseDto> updatePost(
            @PathVariable Long id,
            @RequestBody @Valid PostRequestDto request) {
        PostResponseDto responseDto = postService.updatePost(id, request);
        return ApiResponse.onSuccess(responseDto);
    }

    // 게시글 삭제 (DELETE) - 비밀번호를 쿼리 파라미터(?password=...)로 받음
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePost(
            @PathVariable Long id,
            @RequestParam String password) {
        postService.deletePost(id, password);
        return ApiResponse.onSuccess(null);
    }
}