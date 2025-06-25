package com.lime.server.mq.deadLetter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lime.server.config.mq.RabbitConfig;
import com.lime.server.mq.dto.BusStopMessage;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeadLetterConsumer {
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitConfig.BUS_DLQ_ROUTING_KEY)
    public void handleDeadLetter(Message message) {
        try {
            // 메시지 본문 역직렬화
            BusStopMessage busStopMessage = objectMapper.readValue(
                    message.getBody(), BusStopMessage.class);

            // 모든 헤더 정보 가져오기
            MessageProperties properties = message.getMessageProperties();
            Map<String, Object> headers = properties.getHeaders();

            log.warn("DLQ_MESSAGE_RECEIVED - NodeId: {}, CityCode: {}, Headers: {}",
                    busStopMessage.getNodeId(),
                    busStopMessage.getCityCode(),
                    headers);

        } catch (Exception e) {
            log.warn("DLQ 메시지 처리 중 오류: {}", e.getMessage(), e);
        }
    }
}
