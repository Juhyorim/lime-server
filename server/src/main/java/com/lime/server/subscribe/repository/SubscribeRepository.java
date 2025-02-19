package com.lime.server.subscribe.repository;

import com.lime.server.auth.Member;
import com.lime.server.subscribe.Subscription;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscribeRepository extends JpaRepository<Subscription, Integer> {
    Optional<Subscription> findByMemberAndNodeIdAndRouteId(Member member, String nodeId, String routeId);
}
