package com.lime.server.subscribe.repository;

import com.lime.server.auth.entity.Member;
import com.lime.server.subscribe.entity.Subscription;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscribeRepository extends JpaRepository<Subscription, Integer> {
    Optional<Subscription> findByMemberAndNodeIdAndRouteId(Member member, String nodeId, String routeId);
    Optional<Subscription> findByMemberAndNodeId(Member member, String nodeId);

    List<Subscription> findByMember(Member member);
}
