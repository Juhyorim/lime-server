package com.lime.server.mq.controller;

import com.lime.server.config.mq.RabbitConfig;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class QueueMonitorController {

    private final AmqpAdmin amqpAdmin;

    @GetMapping("/queue/status")
    public Map<String, Object> getQueueStatus() {
        Properties queueProperties = amqpAdmin.getQueueProperties(RabbitConfig.BUS_QUEUE);

        Map<String, Object> status = new HashMap<>();
        status.put("messageCount", queueProperties.get("QUEUE_MESSAGE_COUNT"));
        status.put("consumerCount", queueProperties.get("QUEUE_CONSUMER_COUNT"));

        return status;
    }
}
