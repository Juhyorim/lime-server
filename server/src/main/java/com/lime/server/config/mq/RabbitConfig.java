package com.lime.server.config.mq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {
    public static final String BUS_QUEUE = "bus.info.queue";
    public static final String BUS_EXCHANGE = "bus.info.exchange";
    public static final String BUS_ROUTING_KEY = "bus.info.routing.key";
    public static final String BUS_DLX = "bus.info.dlx";
    public static final String BUS_DLQ_ROUTING_KEY = "bus.info.dlq";

    @Bean
    public Queue busQueue() {
        return QueueBuilder.durable(BUS_QUEUE)
               .deadLetterExchange(BUS_DLX)
                .deadLetterRoutingKey(BUS_DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    public Binding binding() {
        return BindingBuilder
                .bind(busQueue())
                .to(busExchange())
                .with(BUS_ROUTING_KEY);
    }

    @Bean
    public DirectExchange busExchange() {
        return new DirectExchange(BUS_EXCHANGE);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(BUS_DLX);
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(BUS_DLQ_ROUTING_KEY).build();
    }

    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder
                .bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with(BUS_DLQ_ROUTING_KEY);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        return factory;
    }
}
