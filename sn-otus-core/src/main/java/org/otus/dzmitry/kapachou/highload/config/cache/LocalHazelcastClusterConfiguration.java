package org.otus.dzmitry.kapachou.highload.config.cache;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.otus.dzmitry.kapachou.highload.model.Tweet;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.LoggingCacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.session.FlushMode;
import org.springframework.session.MapSession;
import org.springframework.session.SaveMode;
import org.springframework.session.hazelcast.HazelcastIndexedSessionRepository;
import org.springframework.session.hazelcast.HazelcastSessionSerializer;
import org.springframework.session.hazelcast.PrincipalNameExtractor;
import org.springframework.session.hazelcast.config.annotation.web.http.EnableHazelcastHttpSession;

import java.util.Collection;

@Configuration(proxyBeanMethods = false)
@EnableCaching
@Profile("local")
@EnableHazelcastHttpSession
public class LocalHazelcastClusterConfiguration extends CachingConfigurerSupport {

    @Bean("basic-tweets_feed-Cache")
    @DependsOn("hazelcastInstance")
    @ConditionalOnClass(HazelcastInstance.class)
    IMap<Long, Collection<Tweet>> tweetsCache(HazelcastInstance hazelcastInstance) {
        return hazelcastInstance.getMap("basic-tweets-feed");
    }

    @Bean
    public HazelcastInstance hazelcastInstance() {
        Config config = new Config();
        config.setClusterName("DEV");
        config.setInstanceName("hz-trading-instance");

        config.getNetworkConfig()
                .setPortAutoIncrement(false)
                .setPort(5701)
                .setJoin(new JoinConfig()
                        .setTcpIpConfig(
                                new TcpIpConfig()
                                        .setEnabled(true)
                                        .setRequiredMember("127.0.0.1")));
        // session config setup
        AttributeConfig attributeConfig = new AttributeConfig()
                .setName(HazelcastIndexedSessionRepository.PRINCIPAL_NAME_ATTRIBUTE)
                .setExtractorClassName(PrincipalNameExtractor.class.getName());
        config.getMapConfig(HazelcastIndexedSessionRepository.DEFAULT_SESSION_MAP_NAME)
                .addAttributeConfig(attributeConfig)
                .addIndexConfig(
                        new IndexConfig(IndexType.HASH, HazelcastIndexedSessionRepository.PRINCIPAL_NAME_ATTRIBUTE));

        SerializerConfig serializerConfig = new SerializerConfig();
        serializerConfig.setImplementation(new HazelcastSessionSerializer()).setTypeClass(MapSession.class);
        config.getSerializationConfig().addSerializerConfig(serializerConfig);

        return Hazelcast.getOrCreateHazelcastInstance(config);
    }

    @Bean
    HazelcastIndexedSessionRepository indexedSessionRepository(HazelcastInstance hazelcastInstance) {
        var sessionRepository = new HazelcastIndexedSessionRepository(hazelcastInstance);
        sessionRepository.setFlushMode(FlushMode.IMMEDIATE);
        sessionRepository.setSaveMode(SaveMode.ALWAYS);
        sessionRepository.setSessionMapName(HazelcastIndexedSessionRepository.DEFAULT_SESSION_MAP_NAME);
        return sessionRepository;
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return new LoggingCacheErrorHandler();
    }

}
