package com.study.springbootstudy.service;

import com.study.springbootstudy.common.exception.GeneralException;
import com.study.springbootstudy.domain.Member;
import com.study.springbootstudy.domain.Post;
import com.study.springbootstudy.dto.PostRequestDto;
import com.study.springbootstudy.dto.PostResponseDto;
import com.study.springbootstudy.repository.MemberRepository;
import com.study.springbootstudy.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository; // 회원 정보를 DB에서 찾기 위해 추가

    // [생성] 게시글 작성 시, 현재 로그인한 사용자의 이메일을 받아 작성자로 매핑
    @Transactional
    public Long createPost(PostRequestDto request, String email) {
        // 1. 이메일로 현재 로그인한 회원(Member) 엔티티 조회
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException("MEMBER404", "존재하지 않는 회원입니다."));

        // 2. DTO를 Entity로 변환 (비밀번호 삭제, member 객체 주입)
        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .member(member) // 👈 외래키(member_id) 연결!
                .build();

        return postRepository.save(post).getId();
    }

    // [조회] 단건 조회 (변경 없음)
    public PostResponseDto getPostDetail(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException("POST404", "해당 게시글이 존재하지 않습니다."));
        return PostResponseDto.from(post);
    }

    // [수정] 수정 시, 현재 로그인한 이메일을 넘겨받아 작성자 본인인지 확인
    @Transactional
    public PostResponseDto updatePost(Long postId, PostRequestDto request, String email) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException("POST404", "해당 게시글이 존재하지 않습니다."));

        // 1. 작성자 검증 (비밀번호 대신 토큰 이메일 사용)
        post.validateAuthor(email);

        // 2. 수정 로직 수행
        post.update(request.getTitle(), request.getContent());

        return PostResponseDto.from(post);
    }

    // [삭제] 삭제 시, 현재 로그인한 이메일을 넘겨받아 작성자 본인인지 확인
    @Transactional
    public void deletePost(Long postId, String email) { // password 파라미터 삭제, email 추가
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException("POST404", "해당 게시글이 존재하지 않습니다."));

        // 1. 작성자 검증
        post.validateAuthor(email);

        // 2. 삭제
        postRepository.delete(post);
    }
}