package com.study.springbootstudy.service;

import com.study.springbootstudy.common.exception.GeneralException;
import com.study.springbootstudy.domain.Comment;
import com.study.springbootstudy.domain.Post;
import com.study.springbootstudy.dto.CommentRequestDto;
import com.study.springbootstudy.repository.CommentRepository;
import com.study.springbootstudy.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    // [댓글 생성]
    @Transactional
    public Long createComment(Long postId, CommentRequestDto request) {
        // 1. 부모 게시글이 있는지 먼저 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException("POST404", "해당 게시글이 존재하지 않습니다."));

        // 2. 댓글 생성 및 게시글과 연결
        Comment comment = Comment.builder()
                .content(request.getContent())
                .password(request.getPassword())
                .post(post) // 핵심! 어떤 게시글의 댓글인지 매핑
                .build();

        return commentRepository.save(comment).getId();
    }

    // [댓글 삭제]
    @Transactional
    public void deleteComment(Long commentId, String password) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new GeneralException("COMMENT404", "해당 댓글이 존재하지 않습니다."));

        // 1. 비밀번호 검증
        comment.validatePassword(password);

        // 2. 삭제
        commentRepository.delete(comment);
    }
}