package com.study.springbootstudy.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ExternalTodoResponse {
    private Long userId;
    private Long id;
    private String title;
    private Boolean completed;
}