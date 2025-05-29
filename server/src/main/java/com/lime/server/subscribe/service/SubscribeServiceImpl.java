package com.lime.server.subscribe.service;

import com.lime.server.auth.entity.Member;
import com.lime.server.auth.repository.MemberRepository;
import com.lime.server.busApi.dto.apiResponse.BusArriveApiResponse;
import com.lime.server.busApi.dto.apiResponse.BusArriveApiResponse.Items;
import com.lime.server.busApi.service.BusApiService;
import com.lime.server.subscribe.entity.BusArriveInfo;
import com.lime.server.subscribe.entity.Subscription;
import com.lime.server.subscribe.entity.SubscriptionType;
import com.lime.server.subscribe.repository.BusArriveInfoCustomRepository;
import com.lime.server.subscribe.repository.BusArriveInfoRepository;
import com.lime.server.subscribe.repository.SubscribeRepository;
import com.lime.server.util.KoreanTimeUtil;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class SubscribeServiceImpl implements SubscribeService {
    private final SubscribeRepository subscribeRepository;
    private final BusArriveInfoRepository busArriveInfoRepository;
    private final BusArriveInfoCustomRepository busArriveInfoCustomRepository;
    private final MemberRepository memberRepository;
    private final BusApiService busApiService;
    private final KoreanTimeUtil timeUtil;

    @Override
    public void subscribe(Member member, String stationId, String routeId, String nodeName, String nodeNo, int cityCode,
                          String routeNo) {
        Member findMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> new IllegalArgumentException("일치하는 회원 없음"));

        Subscription subscription = subscribeRepository.findByMemberAndNodeIdAndRouteId(findMember, stationId, routeId)
                .orElseGet(() -> null);

        if (subscription != null) {
            throw new IllegalArgumentException("이미 존재하는 구독정보");
        }

        subscription = Subscription.of(findMember, cityCode, stationId, nodeNo, nodeName, routeId, routeNo);
        findMember.addSubscription(subscription);

        subscribeRepository.save(subscription);
    }

    @Override
    public void subscribeV2(Member member, String stationId, String nodeName, String nodeNo, int cityCode) {
        Member findMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> new IllegalArgumentException("일치하는 회원 없음"));

        Subscription subscription = subscribeRepository.findByMemberAndNodeId(findMember, stationId)
                .orElseGet(() -> null);

        if (subscription != null) {
            throw new IllegalArgumentException("이미 존재하는 구독정보");
        }

        subscription = Subscription.of(findMember, cityCode, stationId, nodeNo, nodeName);
        findMember.addSubscription(subscription);
        subscribeRepository.save(subscription);
    }

    @Override
    public List<Subscription> getList(Member member) {
        Member findMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> new IllegalArgumentException("일치하는 회원 없음"));

        List<Subscription> subscriptions = subscribeRepository.findByMember(findMember);

        return subscriptions;
    }

    @Override //구독정보로 버스 도착 시간 찾기
    public List<BusArriveInfo> getBusInfo(Member member, Integer subscriptionId) {
        Subscription subscription = subscribeRepository.findById(subscriptionId)
                .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 구독정보"));

        if (subscription.getRouteId() == null || subscription.getRouteId().isEmpty()) {
            return busArriveInfoRepository.findByCityCodeAndNodeId(
                    subscription.getCityCode(), subscription.getNodeId());
        }

        return busArriveInfoRepository.findByCityCodeAndNodeId(
                subscription.getCityCode(), subscription.getNodeId());
    }

    @Override //도시코드, 정류장, 버스로 도착시간 찾기
    public List<BusArriveInfo> getBusInfo(int cityCode, String nodeId, String routeId) {
        return busArriveInfoRepository.findByCityCodeAndNodeIdAndRouteId(
                cityCode, nodeId, routeId);
    }

    @Override
    public List<BusArriveInfo> getBusInfoWithDate(int cityCode, String nodeId, String routeId, LocalDate localDate) {
        LocalDateTime startOfDay = localDate.atStartOfDay(); // 00:00:00
        LocalDateTime endOfDay = localDate.plusDays(1).atStartOfDay();
        log.info("time:" + startOfDay + " " + endOfDay);

        return busArriveInfoCustomRepository.findByCityCodeAndNodeIdAndRouteIdAndDate(
                cityCode, nodeId, routeId, localDate);
    }

    @Override
    public void cancel(Member member, Integer subscribeId) {
        Subscription subscription = subscribeRepository.findById(subscribeId)
                .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 구독"));

        if (!subscription.getMember().getId().equals(member.getId())) {
            throw new AccessDeniedException("권한이 없는 유저");
        }

        subscribeRepository.delete(subscription);
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
                    LocalDateTime arriveTime = timeUtil.getCurrentDateTime().plusSeconds(arriveBus.getArrtime());

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
                        LocalDateTime arriveTime = timeUtil.getCurrentDateTime().plusSeconds(arriveBus.getArrtime());

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
}
