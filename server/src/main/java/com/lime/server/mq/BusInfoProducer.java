package com.lime.server.mq;

import com.lime.server.config.mq.RabbitConfig;
import com.lime.server.mq.dto.BusStopMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BusInfoProducer {
    private final String MESSAGE_TTL = "420000";
    private final RabbitTemplate rabbitTemplate;

    public void sendBusStopMessage(BusStopMessage message) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.BUS_EXCHANGE,
                RabbitConfig.BUS_ROUTING_KEY,
                message,
                messagePostProcessor -> {
                    messagePostProcessor.getMessageProperties()
                            .setExpiration(MESSAGE_TTL); //7분 TTL
                    return messagePostProcessor;
                }
        );
//        log.info("메시지 발송: {}", message);
    }

    // 우선순위 메시지 발송
    public void sendPriorityMessage(BusStopMessage message, int priority) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.BUS_EXCHANGE,
                RabbitConfig.BUS_ROUTING_KEY,
                message,
                messagePostProcessor -> {
                    messagePostProcessor.getMessageProperties().setPriority(priority);
                    messagePostProcessor.getMessageProperties().setExpiration(MESSAGE_TTL); //7분 TTL
                    return messagePostProcessor;
                }
        );
    }
}
