package com.study.springbootstudy.controller;

import com.study.springbootstudy.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {
    @GetMapping("/api/ping")
    public ApiResponse<String> ping() {
        return ApiResponse.onSuccess("pong");
    }
}