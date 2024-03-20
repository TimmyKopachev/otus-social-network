package org.otus.dzmitry.kapachou.highload.pipeline;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.errors.StreamsException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

@Slf4j
public class KafkaStreamsOrchestrator implements DisposableBean, InitializingBean {

    final Map<String, KafkaStreams> streams = new HashMap<>();

    private final ForkJoinPool forkJoinPool = new ForkJoinPool(20);
    private final ExecutorService executorService = Executors.newFixedThreadPool(20);

    @Override
    public void destroy() {
        streams.values().forEach(this::closeKafkaStream);
    }

    @Override
    public void afterPropertiesSet() {
        forkJoinPool.submit(() -> streams.entrySet().parallelStream().forEach(this::runKafkaStream));
    }

    public void savePipeline(Topology topology, Properties properties) {
        KafkaStreams kafkaStreams = new KafkaStreams(topology, properties);
        streams.put(properties.getProperty(StreamsConfig.APPLICATION_ID_CONFIG), kafkaStreams);
    }

    private void closeKafkaStream(KafkaStreams kafkaStreams) {
        kafkaStreams.close(Duration.ofSeconds(5));
        safeStreamCleanup(kafkaStreams);
    }

    private void runKafkaStream(Map.Entry<String, KafkaStreams> kafkaStreams) {
        safeStreamCleanup(kafkaStreams.getValue());
        CompletableFuture.runAsync(() -> kafkaStreams.getValue().start(), executorService);
    }

    private void safeStreamCleanup(KafkaStreams kafkaStreams) {
        try {
            kafkaStreams.cleanUp();
        } catch (StreamsException exception) {
            log.warn("The stream: {} can not be cleaned up", exception.getMessage());
        }
    }
}
