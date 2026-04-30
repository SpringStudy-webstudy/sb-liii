package com.study.springbootstudy.controller;

import com.study.springbootstudy.common.ApiResponse;
import com.study.springbootstudy.dto.CommentRequestDto;
import com.study.springbootstudy.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 생성
    @PostMapping("/api/posts/{postId}/comments")
    public ApiResponse<Long> createComment(
            @PathVariable Long postId,
            @RequestBody @Valid CommentRequestDto request) {
        Long commentId = commentService.createComment(postId, request);
        return ApiResponse.onSuccess(commentId);
    }

    // 댓글 삭제
    @DeleteMapping("/api/comments/{commentId}")
    public ApiResponse<Void> deleteComment(
            @PathVariable Long commentId,
            @RequestParam String password) {
        commentService.deleteComment(commentId, password);
        return ApiResponse.onSuccess(null);
    }
}