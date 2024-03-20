package org.otus.dzmitry.kapachou.highload.autoconfig;

import org.otus.dzmitry.kapachou.highload.pipeline.KafkaStreamsOrchestrator;
import org.otus.dzmitry.kapachou.highload.pipeline.StreamPipeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.kafka.annotation.EnableKafka;

import java.util.List;
import java.util.Map;

@EnableKafka
@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnExpression("${messaging.config.enabled:false}")
public class MessagingAutoConfiguration {

    @Bean
    KafkaStreamsOrchestrator kafkaStreamsOrchestrator(@Autowired List<StreamPipeline> pipelines) {
        var orchestrator = new KafkaStreamsOrchestrator();

        pipelines.forEach(
                pipeline -> orchestrator.savePipeline(pipeline.buildTopology(), pipeline.initProperties()));

        return orchestrator;
    }

}