package org.otus.dzmitry.kapachou.highload.config.feign;

import feign.Client;
import feign.Contract;
import feign.Feign;
import feign.Logger;
import feign.slf4j.Slf4jLogger;
import lombok.extern.slf4j.Slf4j;
import org.otus.dzmitry.kapachou.highload.jpa.AuthenticationCachedPersonService;
import org.otus.dzmitry.kapachou.highload.web.external.DialogueExternalRestApi;
import org.springframework.cloud.openfeign.CachingCapability;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
@Slf4j
public class SnFeignClientConfiguration {

    @Bean
    JwtTokenRequestInterceptor requestInterceptor(AuthenticationCachedPersonService authenticationService) {
        return new JwtTokenRequestInterceptor(authenticationService);
    }

    @Bean
    Contract contract() {
        return new SpringMvcContract();
    }

    @Bean
    Logger logger() {
        return new Slf4jLogger(log);
    }

//    @Bean
//    DialogueExternalRestApi dialogueExternalRestApi(Client client, AuthenticationCachedPersonService authenticationService) {
//        return Feign.builder()
//                .client(client)
//                .logLevel(Logger.Level.FULL)
//                .contract(new SpringMvcContract())
//                .requestInterceptor(new JwtTokenRequestInterceptor(authenticationService))
//                .target(DialogueExternalRestApi.class, "http://sn-otus-dialogue-chat/dialogues");
//    }


}
