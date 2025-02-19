package com.lime.server.subscribe.service;

import com.lime.server.auth.Member;
import com.lime.server.auth.repository.MemberRepository;
import com.lime.server.subscribe.Subscription;
import com.lime.server.subscribe.repository.SubscribeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class SubscribeServiceImpl implements SubscribeService {
    //memberId는 1로 고정
    private static final int MEMBER_ID = 1;

    private final SubscribeRepository subscribeRepository;
    private final MemberRepository memberRepository;

    @Override
    public void subscribe(String stationId, String routeId, String nodeName, String nodeNo, int cityCode) {
        Member findMember = memberRepository.findById(MEMBER_ID)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 회원 없음"));

        Subscription subscription = subscribeRepository.findByMemberAndNodeIdAndRouteId(findMember, stationId, routeId)
                .orElseGet(() -> null);

        if (subscription != null) {
            throw new IllegalArgumentException("이미 존재하는 구독정보");
        }

        subscription = Subscription.of(findMember, cityCode, stationId, nodeNo, nodeName, routeId);
        subscribeRepository.save(subscription);
    }
}
