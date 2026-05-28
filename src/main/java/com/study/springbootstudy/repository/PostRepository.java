package com.study.springbootstudy.repository;

import com.study.springbootstudy.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {

    // N+1 문제를 해결하기 위한 Fetch Join 쿼리 + 페이징 처리
    // 게시글(Post)을 조회할 때 연관된 회원(Member) 정보도 한 번의 쿼리로 다 가져옵니다.
    @Query(value = "SELECT p FROM Post p JOIN FETCH p.member",
            countQuery = "SELECT count(p) FROM Post p")
    Page<Post> findAllWithMember(Pageable pageable);
}