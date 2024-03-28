package org.otus.dzmitry.kapachou.highload.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.session.hazelcast.HazelcastIndexedSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

@Configuration
@AllArgsConstructor
public class WebApplicationConfiguration {

    final HazelcastIndexedSessionRepository hzSessionRepository;

//    SecurityContextRepository securityContextRepository() {
//        HazelcastIndexedSessionRepository
//        var repo =  new HttpSessionSecurityContextRepository();
//        return repo;
//    }
//

    @Bean
    public SpringSessionBackedSessionRegistry<?> hzSessionRepository() {
        return new SpringSessionBackedSessionRegistry<>(hzSessionRepository);
    }
}
