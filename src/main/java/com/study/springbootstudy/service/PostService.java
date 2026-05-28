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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.study.springbootstudy.dto.PostPageResponseDto;
import com.study.springbootstudy.repository.CommentRepository;
import com.study.springbootstudy.domain.Comment;
import com.study.springbootstudy.dto.PostWithCommentRequestDto;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    // [목록 조회] 페이지네이션 및 N+1 해결 적용
    public PostPageResponseDto getPostList(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Post> postPage = postRepository.findAllWithMember(pageable);
        return PostPageResponseDto.from(postPage);
    }

    // [생성] 게시글 작성 시, 현재 로그인한 사용자의 이메일을 받아 작성자로 매핑
    @Transactional
    public Long createPost(PostRequestDto request, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException("MEMBER404", "존재하지 않는 회원입니다."));

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .member(member)
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

        post.validateAuthor(email);
        post.update(request.getTitle(), request.getContent());

        return PostResponseDto.from(post);
    }

    // [삭제] 삭제 시, 현재 로그인한 이메일을 넘겨받아 작성자 본인인지 확인
    @Transactional
    public void deletePost(Long postId, String email) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException("POST404", "해당 게시글이 존재하지 않습니다."));

        post.validateAuthor(email);
        postRepository.delete(post);
    }

    // [7주차 핵심 미션] 게시글 + 초기 댓글 동시 저장 (비즈니스 예외 케이스 3단 방어막 적용)
    @Transactional // 하나의 트랜잭션으로 묶여 있어 중간에 예외 케이스에 걸리면 전체 롤백됩니다.
    public Long createPostWithComment(PostWithCommentRequestDto request, String email) {

        // [예외 케이스 1] 회원 검증: 토큰의 이메일 유저가 실제 DB에 존재하는지 확인
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException("MEMBER404", "존재하지 않는 회원입니다. 글을 작성할 수 없습니다."));

        // [예외 케이스 2] 비즈니스 룰 검증: 제목과 내용이 완전히 똑같으면 도배 글로 간주하고 차단
        if (request.getTitle().equals(request.getContent())) {
            throw new GeneralException("POST400", "게시글의 제목과 내용은 동일하게 작성할 수 없습니다.");
        }

        // [예외 케이스 3] 금지어 필터링: 첫 댓글 내용에 무분별한 광고성 키워드가 포함되어 있으면 차단
        if (request.getCommentContent().contains("광고") || request.getCommentContent().contains("추천인")) {
            throw new GeneralException("COMMENT400", "댓글에 제한된 단어(광고/홍보성 문구)가 포함되어 있습니다.");
        }

        // --- 🛡️ 모든 예외 케이스(검문소)를 무사히 통과하면 DB 저장 로직 수행 ---

        // 1. 게시글(Post) 생성 및 1차 저장
        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .member(member)
                .build();
        Post savedPost = postRepository.save(post);

        // 2. 댓글(Comment) 생성 및 2차 저장 (방금 저장한 게시글 객체와 매핑)
        Comment comment = Comment.builder()
                .content(request.getCommentContent())
                .post(savedPost)
                .member(member)
                .build();
        commentRepository.save(comment);

        // 3. 성공 시 생성된 게시글 ID 반환
        return savedPost.getId();
    }
}