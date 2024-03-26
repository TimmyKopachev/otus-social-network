package org.otus.dzmitry.kapachou.highload.config.feign;

import feign.Feign;
import feign.Logger;
import feign.RequestInterceptor;
import org.otus.dzmitry.kapachou.highload.jpa.AuthenticationCachedPersonService;
import org.otus.dzmitry.kapachou.highload.web.external.DialogueExternalRestApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfiguration {

    @Autowired
    AuthenticationCachedPersonService authenticationService;

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    RequestInterceptor basicAuthRequestInterceptor() {
        return new CachedAuthenticationRequestInterceptor(authenticationService);
    }

    @Bean
    DialogueExternalRestApi dialogueExternalRestApi() {
        return Feign.builder()
                .contract(new SpringMvcContract())
                .logLevel(feignLoggerLevel())
                .requestInterceptor(basicAuthRequestInterceptor())
                .target(DialogueExternalRestApi.class, "http://sn-otus-dialogue-chat/dialogues");
    }


}
