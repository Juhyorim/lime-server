package com.lime.server.subscribe.config;

import com.lime.server.subscribe.service.SubscribeService;
import java.io.IOException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfig {
    private final SubscribeService subscribeService;

    @Scheduled(fixedDelay = 90000)
    public void run() throws IOException {
        if (isNightTime()) {
            return; // 밤 11시 이후이거나 아침 6시 이전인 경우 API 호출 x
        }

        subscribeService.getSubscribedBusInfo();
    }

    private boolean isNightTime() {
        ZonedDateTime koreaTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        LocalTime currentTime = koreaTime.toLocalTime();
        log.info("한국 현재시간: " + currentTime);
        LocalTime nightStart = LocalTime.of(23, 0); // 밤 11시
        LocalTime morningEnd = LocalTime.of(6, 0);  // 아침 6시

        return currentTime.isAfter(nightStart) || currentTime.isBefore(morningEnd);
    }
}
