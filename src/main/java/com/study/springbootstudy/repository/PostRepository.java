package com.study.springbootstudy.repository;

import com.study.springbootstudy.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository를 상속받으면 기본적인 CRUD(생성, 조회, 수정, 삭제) 기능이 생긴다
public interface PostRepository extends JpaRepository<Post, Long> {
}