//package com.lime.server.mq;
//
//import com.lime.server.config.mq.RabbitConfig;
//import com.lime.server.mq.dto.BusStopMessage;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.Queue;
//import org.springframework.amqp.core.QueueBuilder;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Slf4j
//@Configuration
//public class RabbitErrorConfig {
////
////    @Bean
////    public Queue deadLetterQueue() {
////        return QueueBuilder.durable("bus.dlq").build();
////    }
////
////    @Bean
////    public Queue busQueueWithDLQ() {
////        return QueueBuilder.durable(RabbitConfig.BUS_QUEUE)
////                .withArgument("x-dead-letter-exchange", "bus.dlx")
////                .withArgument("x-dead-letter-routing-key", "bus.dlq")
////                .build();
////    }
////
////    @RabbitListener(queues = "bus.dlq")
////    public void handleFailedMessages(BusStopMessage message) {
////        log.warn("처리 실패한 메시지: {}", message);
////        // 관리자 알림, 수동 처리 로직 등
////    }
//}
