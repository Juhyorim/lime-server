package com.lime.server.subscribe.config;

import com.lime.server.mq.BusInfoProducer;
import com.lime.server.mq.dto.BusStopMessage;
import com.lime.server.subscribe.entity.ArrangedBusArriveInfo;
import com.lime.server.subscribe.entity.BusArriveInfo;
import com.lime.server.subscribe.entity.Subscription;
import com.lime.server.subscribe.repository.ArrangedBusArriveInfoRepository;
import com.lime.server.subscribe.repository.SubscribeRepository;
import com.lime.server.subscribe.service.SubscribeService;
import com.lime.server.util.KoreanTimeUtil;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfig {
    private final KoreanTimeUtil timeUtil;
    private final SubscribeService subscribeService;
    private final SubscribeRepository subscribeRepository;
    private final BusInfoProducer busInfoProducer;
    private final ArrangedBusArriveInfoRepository arrangedBusArriveInfoRepository;

    @Scheduled(fixedDelay = 300000) //5분
    public void run() throws IOException {
        if (timeUtil.isNightTime()) {
            return; // 밤 11시 이후이거나 아침 6시 이전인 경우 API 호출 x
        }

        List<Subscription> subscriptions = subscribeRepository.findAll();

        for (Subscription subscription : subscriptions) {
            BusStopMessage message = BusStopMessage.of(
                    subscription.getCityCode(),
                    subscription.getNodeId(),
                    subscription.getNodeNo()
            );

            busInfoProducer.sendBusStopMessage(message);
        }

        log.info("{}개 정류소 MQ 메시지 발송 완료", subscriptions.size());
    }

//    @Scheduled(fixedDelay = 3000000)
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul") //밤 12시 정각에 수행 //@TODO 제대로 동작하는 알고리즘 찾으면 켜기
    public void cleanBusInfo() {
        List<Subscription> subscriptions = subscribeRepository.findAll();

        log.info("12시 정돈 스케줄링 시작, 시간 {}", timeUtil.getCurrentDateTime());
        for (Subscription subscription : subscriptions) {
            log.info("구독번호 {} 정돈시작", subscription.getId());
//            if (subscription.getCityCode() != 37050) { //테스트용 코드
//                continue;
//            }
//            if (!subscription.getNodeId().equals("GMB4")) {
//                continue;
//            }
//            for (int t=1; t<30; t++) {
//                LocalDate findTime = timeUtil.getCurrentDateTime().toLocalDate().minusDays(t);

            try {
                List<BusArriveInfo> busArriveInfoList = subscribeService.getBusInfoYesterday(subscription.getCityCode(),
                        subscription.getNodeId());

                if (busArriveInfoList.isEmpty()) {
                    continue; //버스 데이터가 존재하지 않음
                }

                AnalysisResult result = analyzeAndCleanBusData(busArriveInfoList);

                log.info("정류장 {} 정리 완료 - 분석: {}대, 삭제: {}건",
                        subscription.getNodeId(), result.getAnalyzedBusCount(), result.getDeletedRecordCount());

            } catch (Exception e) {
                log.error("처리 실패 - 도시: {}, 정류장: {}",
                        subscription.getCityCode(), subscription.getNodeId(), e);
            }
//            }
        }
        log.info("12시 정돈 스케줄링 끝");
    }

    private AnalysisResult analyzeAndCleanBusData(List<BusArriveInfo> busArriveInfoList) {
        int analyzedBusCount = 0;
        int deletedRecordCount = 0;

        // 1. routeId(버스 번호)별로 그룹화
        Map<String, List<BusArriveInfo>> busByRoute = busArriveInfoList.stream()
                .collect(Collectors.groupingBy(BusArriveInfo::getRouteId));

        // 2. 각 노선별로 분석 및 정리
        for (Map.Entry<String, List<BusArriveInfo>> entry : busByRoute.entrySet()) {
            String routeId = entry.getKey();
            List<BusArriveInfo> routeData = entry.getValue();

            try {
//                log.info("노선 {} 분석 시작 - 총 {}건", routeId, routeData.size());
                // 3. 시간순 정렬 (createdAt 기준)
                routeData.sort(Comparator.comparing(BusArriveInfo::getCreatedAt));

                // 4. 유효한 버스 도착 데이터 찾기
                List<BusArriveInfo> validDataList = findValidBusArrivalData(routeData);

                //5-2. 새로운 버스 도착 데이터로 저장
                addNewData(validDataList);

//                log.info("🗑노선 {} - {}건 유지", routeId, validDataList.size());

                analyzedBusCount += validDataList.size();

            } catch (Exception e) {
                log.warn("노선 {} 분석 실패", routeId, e);
            }
        }

        return new AnalysisResult(analyzedBusCount, deletedRecordCount);
    }

    //유효한 raw data를 새로운 테이블에 저장
    private void addNewData(List<BusArriveInfo> validDataList) {
        List<ArrangedBusArriveInfo> newList = new ArrayList<>();
        for (BusArriveInfo busArriveInfo : validDataList) {
            newList.add(ArrangedBusArriveInfo.from(busArriveInfo));
        }

        arrangedBusArriveInfoRepository.saveAll(newList);
    }

    private List<BusArriveInfo> findValidBusArrivalData(List<BusArriveInfo> routeData) {
        List<BusArriveInfo> validIds = new ArrayList<>();

        if (routeData.size() <= 1) {
            return routeData;
        }

        // createdAt 순으로 정렬된 데이터를 순차적으로 비교하여 세그먼트 분리
        List<List<BusArriveInfo>> busSegments = segmentBusesByConditions(routeData);

        // 각 버스 세그먼트에서 가장 신뢰할 수 있는 데이터 선택
        for (int i = 0; i < busSegments.size(); i++) {
            List<BusArriveInfo> segment = busSegments.get(i);

            if (segment.isEmpty()) continue;

            // 가장 작은 remainTime을 가진 데이터 = 실제 도착시간에 가장 가까운 데이터
            BusArriveInfo mostReliableData = segment.stream()
                    .min(Comparator.comparing(BusArriveInfo::getRemainTime))
                    .orElse(null);

            if (mostReliableData != null) {
                validIds.add(mostReliableData);
//                log.info("time: " + mostReliableData.getArriveTime());
//                log.info("{}번째 버스: {} 도착예정, remainTime={}초 선택 ({}건 중에서)",
//                        i + 1,
//                        mostReliableData.getArriveTime().toString().substring(11, 19),
//                        mostReliableData.getRemainTime(),
//                        segment.size());
            }
        }

//        log.info("총 {}건 중 {}건 유효 데이터 선택", routeData.size(), validIds.size());

        return validIds;
    }

    private List<List<BusArriveInfo>> segmentBusesByConditions(List<BusArriveInfo> data) {
        List<List<BusArriveInfo>> segments = new ArrayList<>();

        if (data.isEmpty()) {
            return segments;
        }

        List<BusArriveInfo> currentSegment = new ArrayList<>();
        currentSegment.add(data.get(0)); // 첫 번째 데이터로 시작

        // createdAt 순으로 정렬된 데이터를 순차적으로 비교
        for (int i = 1; i < data.size(); i++) {
            BusArriveInfo prev = data.get(i - 1);
            BusArriveInfo curr = data.get(i);

            boolean isBusChanged = isBusChangeDetected(prev, curr);

            if (isBusChanged) {
                // 버스 교체 지점 발견 - 현재 세그먼트 완료하고 새 세그먼트 시작
                if (!currentSegment.isEmpty()) {
                    segments.add(new ArrayList<>(currentSegment));
//                    log.info("세그먼트 완료: {}건 ({}~{})",
//                            currentSegment.size(),
//                            currentSegment.get(0).getArriveTime().toString().substring(11, 19),
//                            currentSegment.get(currentSegment.size()-1).getArriveTime().toString().substring(11, 19));
                }
                currentSegment.clear();
                currentSegment.add(curr);
            } else {
                // 같은 버스로 판단 - 현재 세그먼트에 추가
                currentSegment.add(curr);
            }
        }

        // 마지막 세그먼트 추가
        if (!currentSegment.isEmpty()) {
            segments.add(currentSegment);
//            log.info("마지막 세그먼트: {}건 ({}~{})",
//                    currentSegment.size(),
//                    currentSegment.get(0).getArriveTime().toString().substring(11, 19),
//                    currentSegment.get(currentSegment.size()-1).getArriveTime().toString().substring(11, 19));
        }

        return segments;
    }

    /**
     * 버스 교체 조건 검사
     */
    private boolean isBusChangeDetected(BusArriveInfo prev, BusArriveInfo curr) {
        // 1) remainTime이 갑자기 300초(5분) 이상 커질 때
        int remainTimeDiff = curr.getRemainTime() - prev.getRemainTime();
        if (remainTimeDiff >= 300) {
//            log.info("버스 교체 감지 (remainTime 증가): {}초 → {}초 (+{}초)",
//                    prev.getRemainTime(), curr.getRemainTime(), remainTimeDiff);
            return true;
        }

        // 2) arriveTime의 간격이 900초(15분) 이상 클 때
        long arriveTimeDiffSeconds = ChronoUnit.SECONDS.between(prev.getArriveTime(), curr.getArriveTime());
        if (Math.abs(arriveTimeDiffSeconds) >= 900) {
            return true;
        }

        return false;
    }

    @Data
    @AllArgsConstructor
    private static class AnalysisResult {
        private int analyzedBusCount;
        private int deletedRecordCount;
    }
}