package com.lime.server.auth.repository;

import com.lime.server.auth.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {
}
