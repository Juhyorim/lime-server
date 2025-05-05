package com.lime.server.subscribe.service;

import com.lime.server.auth.Member;
import com.lime.server.auth.repository.MemberRepository;
import com.lime.server.busApi.dto.apiResponse.BusArriveApiResponse;
import com.lime.server.busApi.service.BusApiService;
import com.lime.server.subscribe.BusArriveInfo;
import com.lime.server.subscribe.Subscription;
import com.lime.server.subscribe.repository.BusArriveInfoRepository;
import com.lime.server.subscribe.repository.SubscribeRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class SubscribeServiceImpl implements SubscribeService {
    //memberId는 1로 고정
    private static final int MEMBER_ID = 1;

    private final SubscribeRepository subscribeRepository;
    private final BusArriveInfoRepository busArriveInfoRepository;
    private final MemberRepository memberRepository;
    private final BusApiService busApiService;

    @Override
    public void subscribe(String stationId, String routeId, String nodeName, String nodeNo, int cityCode, String routeNo) {
        Member findMember = memberRepository.findById(MEMBER_ID)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 회원 없음"));

        Subscription subscription = subscribeRepository.findByMemberAndNodeIdAndRouteId(findMember, stationId, routeId)
                .orElseGet(() -> null);

        if (subscription != null) {
            throw new IllegalArgumentException("이미 존재하는 구독정보");
        }

        subscription = Subscription.of(findMember, cityCode, stationId, nodeNo, nodeName, routeId, routeNo);
        subscribeRepository.save(subscription);
    }

    @Override
    public List<Subscription> getList() {
        Member findMember = memberRepository.findById(MEMBER_ID)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 회원 없음"));

        List<Subscription> subscriptions = subscribeRepository.findByMember(findMember);

        return subscriptions;
    }

    @Override
    public void getSubscribedBusInfo() throws IOException {
        List<Subscription> subscriptions = subscribeRepository.findAll();

        for (Subscription subscription : subscriptions) {
            BusArriveApiResponse arriveBuses = busApiService.getArriveBuses(subscription.getCityCode(),
                    subscription.getNodeId());

            for (BusArriveApiResponse.ArriveBus arriveBus: arriveBuses.getResponse().getBody().getItems().getItem()) {
                if (arriveBus.getRouteid().equals(subscription.getRouteId())) {
                    LocalDateTime arriveTime = LocalDateTime.now().plusSeconds(arriveBus.getArrtime());

                    BusArriveInfo busArriveInfo = BusArriveInfo.of(
                        arriveTime,
                            subscription.getCityCode(),
                            subscription.getNodeId(),
                            subscription.getNodeNo(),
                            arriveBus.getNodenm(),
                            arriveBus.getRouteid(),
                            arriveBus.getArrtime()
                    );
                    busArriveInfoRepository.save(busArriveInfo);
                }
            }
        }
    }
}
