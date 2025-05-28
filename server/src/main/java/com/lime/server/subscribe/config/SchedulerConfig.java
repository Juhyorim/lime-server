package com.lime.server.subscribe.config;

import com.lime.server.subscribe.service.SubscribeService;
import com.lime.server.util.KoreanTimeUtil;
import java.io.IOException;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfig {
    private static final LocalTime NIGHT_START = LocalTime.of(23, 0);
    private static final LocalTime MORNING_END = LocalTime.of(6, 0);

    private final KoreanTimeUtil timeUtil;
    private final SubscribeService subscribeService;

    @Scheduled(fixedDelay = 90000)
    public void run() throws IOException {
        if (timeUtil.isNightTime()) {
            return; // 밤 11시 이후이거나 아침 6시 이전인 경우 API 호출 x
        }

        subscribeService.getSubscribedBusInfo();
    }
}
