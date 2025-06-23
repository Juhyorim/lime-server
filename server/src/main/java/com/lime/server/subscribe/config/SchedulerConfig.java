package com.lime.server.subscribe.config;

import com.lime.server.mq.BusInfoProducer;
import com.lime.server.mq.dto.BusStopMessage;
import com.lime.server.subscribe.entity.Subscription;
import com.lime.server.subscribe.repository.SubscribeRepository;
import com.lime.server.subscribe.service.SubscribeService;
import com.lime.server.util.KoreanTimeUtil;
import java.io.IOException;
import java.util.List;
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

    @Scheduled(fixedDelay = 90000)
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
            log.info(subscription.getCityCode() + "/" + subscription.getNodeId() + "/" + subscription.getNodeNo());

            busInfoProducer.sendBusStopMessage(message);
        }

        log.info("{}개 정류소 MQ 메시지 발송 완료", subscriptions.size());
    }
}
