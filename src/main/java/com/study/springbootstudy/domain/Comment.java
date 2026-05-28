package com.study.springbootstudy.domain;

import com.study.springbootstudy.common.exception.GeneralException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private String password; // 댓글 작성자 확인용

    // N:1 관계 매핑 (게시글 하나에 여러 댓글). 지연 로딩으로 성능 최적화
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 권한 검증 로직
    public void validatePassword(String inputPassword) {
        if (!this.password.equals(inputPassword)) {
            throw new GeneralException("AUTH403", "비밀번호가 일치하지 않습니다. 수정/삭제 권한이 없습니다.");
        }
    }
}