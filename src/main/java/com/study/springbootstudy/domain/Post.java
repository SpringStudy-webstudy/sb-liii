package com.study.springbootstudy.domain;

import com.study.springbootstudy.common.exception.GeneralException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// 👇 7주차 미션: 테이블 이름과 인덱스(목차) 설정 추가!
@Table(name = "post", indexes = {
        @Index(name = "idx_post_title", columnList = "title")
})
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String content;

    // 회원(Member)과의 다대일 연관관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    public Post(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // 작성자 본인인지 확인하는 검증 메서드로 변경
    public void validateAuthor(String currentUserEmail) {
        if (!this.member.getEmail().equals(currentUserEmail)) {
            throw new GeneralException("AUTH403", "해당 게시글에 대한 권한이 없습니다.");
        }
    }
}