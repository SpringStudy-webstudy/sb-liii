package com.study.springbootstudy.controller;

import com.study.springbootstudy.common.ApiResponse;
import com.study.springbootstudy.dto.ExternalTodoResponse;
import com.study.springbootstudy.service.ExternalApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/external")
public class ExternalApiController {

    private final ExternalApiService externalApiService;

    @GetMapping("/todo/{id}")
    public ApiResponse<ExternalTodoResponse> getTodo(@PathVariable Long id) {
        ExternalTodoResponse response = externalApiService.getExternalTodo(id);
        return ApiResponse.onSuccess(response);
    }
}