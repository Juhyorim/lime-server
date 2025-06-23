package com.lime.server.mq;

import com.lime.server.busApi.dto.apiResponse.BusArriveApiResponse;
import com.lime.server.busApi.dto.apiResponse.BusArriveApiResponse.Items;
import com.lime.server.busApi.service.BusApiService;
import com.lime.server.config.mq.RabbitConfig;
import com.lime.server.mq.dto.BusStopMessage;
import com.lime.server.subscribe.entity.BusArriveInfo;
import com.lime.server.subscribe.repository.BusArriveInfoRepository;
import com.lime.server.util.KoreanTimeUtil;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BusInfoConsumer {
    private final BusApiService busApiService;
    private final KoreanTimeUtil timeUtil;
    private final BusArriveInfoRepository busArriveInfoRepository;

    // 여러 워커로 병렬 처리
    @RabbitListener(
            queues = RabbitConfig.BUS_QUEUE,
            concurrency = "3-10"  // 최소 3개, 최대 10개 스레드
    )
    public void processBusStop(BusStopMessage message) {
        try {
//            log.info("메시지 처리 시작: {}", message);

            // 버스 API 호출
            BusArriveApiResponse arriveBuses = busApiService.getArriveBuses(message.getCityCode(),
                    message.getNodeId());

            Items items = arriveBuses.getResponse().getBody().getItems();
            if (items == null) {
                return;
            }

            LocalDateTime currentTime = timeUtil.getCurrentDateTime();

            for (BusArriveApiResponse.ArriveBus arriveBus : items.getItem()) {
                LocalDateTime arriveTime = currentTime.plusSeconds(arriveBus.getArrtime());

                BusArriveInfo busArriveInfo = BusArriveInfo.of(
                        arriveTime,
                        message.getCityCode(),
                        message.getNodeId(),
                        message.getNodeNo(),
                        arriveBus.getNodenm(),
                        arriveBus.getRouteid(),
                        arriveBus.getRouteno(),
                        arriveBus.getArrtime(),
                        currentTime
                );

                busArriveInfoRepository.save(busArriveInfo);
            }

//            log.info("메시지 처리 완료: {}", message.getNodeId());

        } catch (Exception e) {
            log.error("메시지 처리 실패: {}", message, e);
            throw new AmqpRejectAndDontRequeueException("처리 실패", e);
        }
    }
}
