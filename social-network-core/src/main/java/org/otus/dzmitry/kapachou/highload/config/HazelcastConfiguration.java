package org.otus.dzmitry.kapachou.highload.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.otus.dzmitry.kapachou.highload.model.Tweet;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.LoggingCacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.Collection;
import java.util.Set;

@Configuration
public class HazelcastConfiguration extends CachingConfigurerSupport {

    @Bean
    public HazelcastInstance hazelcastInstance() {
        return Hazelcast.newHazelcastInstance(hazelCastConfig());
    }

    @Bean("basic-tweets_feed-Cache")
    @DependsOn("hazelcastInstance")
    @ConditionalOnClass(HazelcastInstance.class)
    IMap<Long, Collection<Tweet>> tweetsCache(HazelcastInstance hazelcastInstance) {
        return hazelcastInstance.getMap("basic-tweets-feed");
    }

    private static Config hazelCastConfig() {
        Config config = new Config();
        config.setClusterName("Otus_HZ-Cache");
        config.getNetworkConfig()
                .setPort(5900)
                .setPortAutoIncrement(false);

        var mapConfig = new MapConfig()
                .setName("tweet-timeline-feed")
                .setBackupCount(1);

        config.addMapConfig(mapConfig);
        return config;
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return new LoggingCacheErrorHandler();
    }
}
