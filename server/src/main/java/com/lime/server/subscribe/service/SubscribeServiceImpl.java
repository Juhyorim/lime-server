package com.lime.server.subscribe.service;

import com.lime.server.auth.entity.Member;
import com.lime.server.auth.repository.MemberRepository;
import com.lime.server.busApi.dto.apiResponse.BusArriveApiResponse;
import com.lime.server.busApi.dto.apiResponse.BusArriveApiResponse.Items;
import com.lime.server.busApi.service.BusApiService;
import com.lime.server.subscribe.entity.BusArriveInfo;
import com.lime.server.subscribe.entity.Subscription;
import com.lime.server.subscribe.entity.SubscriptionType;
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
    public void subscribe(String stationId, String routeId, String nodeName, String nodeNo, int cityCode,
                          String routeNo) {
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
    public void subscribeV2(String stationId, String nodeName, String nodeNo, int cityCode) {
        Member findMember = memberRepository.findById(MEMBER_ID)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 회원 없음"));

        Subscription subscription = subscribeRepository.findByMemberAndNodeId(findMember, stationId)
                .orElseGet(() -> null);

        if (subscription != null) {
            throw new IllegalArgumentException("이미 존재하는 구독정보");
        }

        subscription = Subscription.of(findMember, cityCode, stationId, nodeNo, nodeName);
        subscribeRepository.save(subscription);
    }

    @Override
    public List<Subscription> getList() {
        Member findMember = memberRepository.findById(MEMBER_ID)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 회원 없음"));

        List<Subscription> subscriptions = subscribeRepository.findByMember(findMember);

        return subscriptions;
    }

    //스케줄러 로직
    @Override
    public void getSubscribedBusInfo() throws IOException {
        List<Subscription> subscriptions = subscribeRepository.findAll();

        for (Subscription subscription : subscriptions) {
            BusArriveApiResponse arriveBuses = busApiService.getArriveBuses(subscription.getCityCode(),
                    subscription.getNodeId());

            Items items = arriveBuses.getResponse().getBody().getItems();
            if (items == null) {
                return;
            }

            if (subscription.getType().equals(SubscriptionType.ONLY_NODE)){
                for (BusArriveApiResponse.ArriveBus arriveBus : items.getItem()) {
                    LocalDateTime arriveTime = LocalDateTime.now().plusSeconds(arriveBus.getArrtime());

                    BusArriveInfo busArriveInfo = BusArriveInfo.of(
                            arriveTime,
                            subscription.getCityCode(),
                            subscription.getNodeId(),
                            subscription.getNodeNo(),
                            arriveBus.getNodenm(),
                            arriveBus.getRouteid(),
                            arriveBus.getRouteno(),
                            arriveBus.getArrtime()
                    );
                    busArriveInfoRepository.save(busArriveInfo);
                }
            } else if (subscription.getType().equals(SubscriptionType.WITH_ROUTE)) {
                //TODO API를 한 번만 호출할 수단 필요
                for (BusArriveApiResponse.ArriveBus arriveBus : items.getItem()) {
                    if (arriveBus.getRouteid().equals(subscription.getRouteId())) {
                        LocalDateTime arriveTime = LocalDateTime.now().plusSeconds(arriveBus.getArrtime());

                        BusArriveInfo busArriveInfo = BusArriveInfo.of(
                                arriveTime,
                                subscription.getCityCode(),
                                subscription.getNodeId(),
                                subscription.getNodeNo(),
                                arriveBus.getNodenm(),
                                arriveBus.getRouteid(),
                                arriveBus.getRouteno(),
                                arriveBus.getArrtime()
                        );
                        busArriveInfoRepository.save(busArriveInfo);
                    }
                }
            }
        }
    }

    @Override
    public List<BusArriveInfo> getBusInfo(Integer subscriptionId) {
        Subscription subscription = subscribeRepository.findById(subscriptionId)
                .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 구독정보"));

        if (subscription.getRouteId() == null || subscription.getRouteId().isEmpty()) {
            return busArriveInfoRepository.findByCityCodeAndNodeId(
                    subscription.getCityCode(), subscription.getNodeId());
        }

        return busArriveInfoRepository.findByCityCodeAndNodeId(
                subscription.getCityCode(), subscription.getNodeId());
    }

    @Override
    public List<BusArriveInfo> getBusInfo(int cityCode, String nodeId, String routeId) {
        return busArriveInfoRepository.findByCityCodeAndNodeIdAndRouteId(
                cityCode, nodeId, routeId);
    }
}
