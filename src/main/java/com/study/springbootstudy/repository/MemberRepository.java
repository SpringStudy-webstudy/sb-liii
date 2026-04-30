package com.study.springbootstudy.repository;

import com.study.springbootstudy.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 이메일(로그인 아이디)을 통해 회원 정보를 찾는 메서드
    Optional<Member> findByEmail(String email);

    // 중복 가입 방지를 위해 이메일 존재 여부를 확인하는 메서드
    boolean existsByEmail(String email);
}