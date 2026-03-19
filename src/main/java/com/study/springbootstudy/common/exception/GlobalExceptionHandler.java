package com.study.springbootstudy.common.exception;

import com.study.springbootstudy.common.ApiResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. 우리가 직접 던진 GeneralException 처리
    @ExceptionHandler(GeneralException.class)
    public ApiResponse<String> handleGeneralException(GeneralException e) {
        return ApiResponse.onFailure(e.getErrorCode(), e.getMessage(), null);
    }

    // 2. @Valid 검증 실패 시 발생하는 에러 처리 (400 에러)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<String> handleValidationException(MethodArgumentNotValidException e) {
        // 첫 번째 에러 메시지만 가져오기
        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return ApiResponse.onFailure("COMMON400", errorMessage, null);
    }

    // 3. 그 외 예상치 못한 모든 에러 처리
    @ExceptionHandler(Exception.class)
    public ApiResponse<String> handleAllException(Exception e) {
        return ApiResponse.onFailure("COMMON500", "서버 내부 오류가 발생했습니다.", e.getMessage());
    }
}