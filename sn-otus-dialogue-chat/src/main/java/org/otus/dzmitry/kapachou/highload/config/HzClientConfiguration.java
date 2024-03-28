package org.otus.dzmitry.kapachou.highload.config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientConnectionStrategyConfig;
import com.hazelcast.config.SerializerConfig;
import com.hazelcast.core.HazelcastInstance;
import org.apache.catalina.util.SessionConfig;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.session.FlushMode;
import org.springframework.session.MapSession;
import org.springframework.session.SaveMode;
import org.springframework.session.hazelcast.HazelcastIndexedSessionRepository;
import org.springframework.session.hazelcast.HazelcastSessionSerializer;
import org.springframework.session.hazelcast.config.annotation.web.http.EnableHazelcastHttpSession;

@Configuration
@EnableHazelcastHttpSession
public class HzClientConfiguration extends CachingConfigurerSupport {

    @Bean
    public HazelcastInstance hazelcastInstance() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setInstanceName("hz-trading-instance");
        clientConfig.setClusterName("DEV");
        clientConfig.getNetworkConfig()
                .addAddress("127.0.0.1:5701");

        clientConfig.getConnectionStrategyConfig()
                .setAsyncStart(true)
                .setReconnectMode(ClientConnectionStrategyConfig.ReconnectMode.ASYNC);

        SerializerConfig serializerConfig = new SerializerConfig();
        serializerConfig.setImplementation(new HazelcastSessionSerializer()).setTypeClass(MapSession.class);
        clientConfig.getSerializationConfig().addSerializerConfig(serializerConfig);

        return HazelcastClient.getOrCreateHazelcastClient(clientConfig);
    }

    public class SecurityInitializer extends AbstractSecurityWebApplicationInitializer {

        public SecurityInitializer() {
            super(SecurityConfig.class, SessionConfig.class);
        }

    }

    @Bean
    HazelcastIndexedSessionRepository indexedSessionRepository(HazelcastInstance hazelcastInstance) {
        var sessionRepository = new HazelcastIndexedSessionRepository(hazelcastInstance);
        sessionRepository.setFlushMode(FlushMode.IMMEDIATE);
        sessionRepository.setSaveMode(SaveMode.ALWAYS);
        sessionRepository.setSessionMapName(HazelcastIndexedSessionRepository.DEFAULT_SESSION_MAP_NAME);
        return sessionRepository;
    }


}
