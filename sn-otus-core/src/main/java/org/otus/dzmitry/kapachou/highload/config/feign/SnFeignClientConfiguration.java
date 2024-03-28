package org.otus.dzmitry.kapachou.highload.config.feign;

import feign.Contract;
import feign.Logger;
import feign.slf4j.Slf4jLogger;
import lombok.extern.slf4j.Slf4j;
import org.otus.dzmitry.kapachou.highload.jpa.AuthenticationCachedPersonService;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;

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

}
