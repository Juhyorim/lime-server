package com.lime.server.subscribe.config;

import com.lime.server.subscribe.service.SubscribeService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfig {
    private final SubscribeService subscribeService;

    @Scheduled(fixedDelay = 30000)
    public void run() throws IOException {
        subscribeService.getSubscribedBusInfo();
    }
}
