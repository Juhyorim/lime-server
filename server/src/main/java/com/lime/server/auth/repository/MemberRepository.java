package com.lime.server.auth.repository;

import com.lime.server.auth.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findByUsername(String username);
}
