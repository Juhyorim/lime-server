package com.lime.server.subscribe.service;

import com.lime.server.auth.entity.Member;
import com.lime.server.auth.repository.MemberRepository;
import com.lime.server.subscribe.entity.ArrangedBusArriveInfo;
import com.lime.server.subscribe.entity.BusArriveInfo;
import com.lime.server.subscribe.entity.Subscription;
import com.lime.server.subscribe.repository.ArrangedBusArriveInfoCustomRepository;
import com.lime.server.subscribe.repository.BusArriveInfoCustomRepository;
import com.lime.server.subscribe.repository.BusArriveInfoRepository;
import com.lime.server.subscribe.repository.SubscribeRepository;
import com.lime.server.util.KoreanTimeUtil;
import java.time.LocalDate;
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
    private final KoreanTimeUtil timeUtil;
    private final ArrangedBusArriveInfoCustomRepository arrangedBusArriveInfoCustomRepository;

    @Override
    public void subscribeV2(Member member, String stationId, String nodeName, String nodeNo, int cityCode) {
        Member findMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> new IllegalArgumentException("일치하는 회원 없음"));

        //중복 구독 불가능
        boolean exists = subscribeRepository.existsByMemberAndNodeId(findMember, stationId);
        if (exists) {
            throw new IllegalArgumentException("이미 존재하는 구독정보");
        }

        Subscription subscription = Subscription.of(findMember, cityCode, stationId, nodeNo, nodeName);
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

    @Override //도시코드, 정류장으로 도착시간 찾기 - 스케줄러에서 사용
    public List<BusArriveInfo> getBusInfoYesterday(int cityCode, String nodeId) {
        LocalDate yesterday = timeUtil.getCurrentDateTime().toLocalDate().minusDays(1);

        return busArriveInfoCustomRepository.findByCityCodeAndNodeIdAndDate(
                cityCode, nodeId, yesterday);
    }

    @Override //도시코드, 정류장으로 도착시간 찾기 - 스케줄러에서 사용
    public List<BusArriveInfo> getBusInfoWithDate2(int cityCode, String nodeId, LocalDate localDate) {
        return busArriveInfoCustomRepository.findByCityCodeAndNodeIdAndDate(
                cityCode, nodeId, localDate);
    }

    @Override
    public List<BusArriveInfo> getBusInfoWithDate(int cityCode, String nodeId, String routeId, LocalDate localDate) {
//        LocalDateTime startOfDay = localDate.atStartOfDay(); // 00:00:00
//        LocalDateTime endOfDay = localDate.plusDays(1).atStartOfDay();

        return busArriveInfoCustomRepository.findByCityCodeAndNodeIdAndRouteIdAndDate(
                cityCode, nodeId, routeId, localDate);
    }

    @Override
    public List<ArrangedBusArriveInfo> getArrangedBusInfoWithDate(int cityCode, String nodeId, String routeId, LocalDate localDate) {
        return arrangedBusArriveInfoCustomRepository.findByCityCodeAndNodeIdAndRouteIdAndDate(
                cityCode, nodeId, routeId, localDate);
    }

    @Override
    public List<ArrangedBusArriveInfo> getAllBusInfoWithDate(int cityCode, String nodeId, LocalDate localDate) {
        return arrangedBusArriveInfoCustomRepository.findByCityCodeAndNodeIdAndDate(
                cityCode, nodeId, localDate);
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
}
