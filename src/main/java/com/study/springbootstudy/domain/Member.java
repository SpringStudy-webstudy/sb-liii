package com.study.springbootstudy.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 무분별한 객체 생성을 막는 안전장치
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // DB가 부여하는 고유 번호 (PK)

    @Column(nullable = false, unique = true)
    private String email; // 로그인 아이디로 사용할 이메일 (중복 불가)

    @Column(nullable = false)
    private String password; // 암호화되어 저장될 비밀번호

    @Column(nullable = false)
    private String name; // 사용자의 이름 또는 닉네임

    @Builder
    public Member(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}