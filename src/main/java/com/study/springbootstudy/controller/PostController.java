package com.study.springbootstudy.controller;

import com.study.springbootstudy.common.ApiResponse;
import com.study.springbootstudy.dto.PostRequestDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {

    @PostMapping("/api/posts")
    public ApiResponse<String> createPost(@Valid @RequestBody PostRequestDto request) {
        // @Valid가 통과되어야 이 코드가 실행됩니다.
        return ApiResponse.onSuccess("게시글이 성공적으로 생성되었습니다.");
    }
}