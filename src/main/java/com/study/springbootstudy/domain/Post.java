package com.study.springbootstudy.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity // 이 클래스가 DB의 테이블 역할을 한다는 선언
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Post {

    @Id // 기본키(PK)로 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID를 1, 2, 3... 자동으로 늘려줌
    private Long id;

    @Column(nullable = false) // 빈 값을 허용하지 않음
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
}