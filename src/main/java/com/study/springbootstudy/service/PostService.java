package com.study.springbootstudy.service;

import com.study.springbootstudy.common.exception.GeneralException;
import com.study.springbootstudy.domain.Post;
import com.study.springbootstudy.dto.PostRequestDto;
import com.study.springbootstudy.dto.PostResponseDto;
import com.study.springbootstudy.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본적으로 읽기 전용으로 설정하여 성능을 높임
public class PostService {

    private final PostRepository postRepository;

    @Transactional // 데이터를 수정, 저장할 때는 이 어노테이션이 꼭 필요함
    public Long createPost(PostRequestDto request) {
        // 1. DTO를 Entity(Post)로 변환
        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .password(request.getPassword())
                .build();

        // 2. DB에 저장하고, 저장된 게시글의 번호(ID)를 반환
        return postRepository.save(post).getId();
    }

    public PostResponseDto getPostDetail(Long postId) {
        // 1. DB에서 ID로 게시글을 찾고, 없으면 예외를 띄움
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException("POST404", "해당 게시글이 존재하지 않습니다."));

        // 2. 찾은 게시글을 DTO에 담아 컨트롤러로 보냄
        return PostResponseDto.from(post);
    }

    // [수정] 더티 체킹 활용
    @Transactional
    public PostResponseDto updatePost(Long postId, PostRequestDto request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException("POST404", "해당 게시글이 존재하지 않습니다."));

        // 1. 비밀번호 검증
        post.validatePassword(request.getPassword());

        // 2. 수정 (트랜잭션 종료 시 JPA가 알아서 DB에 반영해 줌)
        post.update(request.getTitle(), request.getContent());

        return PostResponseDto.from(post);
    }

    // [삭제]
    @Transactional
    public void deletePost(Long postId, String password) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException("POST404", "해당 게시글이 존재하지 않습니다."));

        // 1. 비밀번호 검증
        post.validatePassword(password);

        // 2. 삭제
        postRepository.delete(post);
    }
}