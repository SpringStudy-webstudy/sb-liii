package com.study.springbootstudy.common; // 패키지 경로 추가

import lombok.AllArgsConstructor; // 임포트 추가
import lombok.Getter;

@Getter // 이 어노테이션이 있어야 아래 필드들을 '사용'하는 것으로 인식함
@AllArgsConstructor // 이 어노테이션이 있어야 '4개의 인수'를 받는 생성자가 만들어짐
public class ApiResponse<T> {
    private boolean isSuccess;
    private String code;
    private String message;
    private T result;

    public static <T> ApiResponse<T> onSuccess(T result) {
        return new ApiResponse<>(true, "COMMON200", "요청에 성공하였습니다.", result);
    }
}
