package org.otus.dzmitry.kapachou.highload.config.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.streams.StreamsConfig;
import org.otus.dzmitry.kapachou.highload.channel.TweetFriendsNotificationRequest;
import org.otus.dzmitry.kapachou.highload.serialization.JacksonSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

@Configuration
public class BasicKafkaConfiguration {

    @Value("${kafka.bootstrap.server:localhost:9092}")
    private String bootstrapServers;

    ProducerFactory<Long, TweetFriendsNotificationRequest> friendsTweetRequestNotificationProducerFactory() {
        return new DefaultKafkaProducerFactory<>(
                Map.of(
                        StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,
                        bootstrapServers,
                        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                        JacksonSerializer.class,
                        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                        JacksonSerializer.class));
    }

    @Bean("tweetSocketNotificationRequestKafkaTemplate")
    KafkaTemplate<Long, TweetFriendsNotificationRequest> tweetSocketNotificationRequestKafkaTemplate() {
        return new KafkaTemplate<>(friendsTweetRequestNotificationProducerFactory());
    }


}
