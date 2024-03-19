package org.otus.dzmitry.kapachou.highload.cache.autoconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.EmbeddedKafkaZKBroker;

import java.util.Objects;

@Profile("local")
@Configuration
@ConditionalOnExpression("${messaging.config.enabled:false}")
public class LocalEmbeddedKafkaConfiguration {

    @Value("${social.network.kafka.topics}")
    private String[] socialNetworkKafkaTopics;

    @Bean
    @ConditionalOnMissingBean
    EmbeddedKafkaBroker embeddedKafkaBroker() {
        Objects.requireNonNull(socialNetworkKafkaTopics);
        var embeddedKafkaBroker = new EmbeddedKafkaZKBroker(1,
                true,
                2,
                socialNetworkKafkaTopics)
                .zkPort(9021)
                .kafkaPorts(9092)
                .zkConnectionTimeout(5000)
                .zkSessionTimeout(5000);

        embeddedKafkaBroker.brokerProperty("listeners", "PLAINTEXT://localhost:9092");
        embeddedKafkaBroker.brokerProperty("port", "9092");
        embeddedKafkaBroker.brokerProperty("compression.type", "producer");

        return embeddedKafkaBroker;
    }
}
