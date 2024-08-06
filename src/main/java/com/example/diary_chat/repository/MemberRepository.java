package com.example.diary_chat.repository;

import com.example.diary_chat.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByNickname(String nickname);

    Optional<Member> findByEmail(String email);
}