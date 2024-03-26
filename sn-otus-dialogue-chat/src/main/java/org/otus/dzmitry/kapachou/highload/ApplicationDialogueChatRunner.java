package org.otus.dzmitry.kapachou.highload;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients(basePackages = "org.otus")
@EnableDiscoveryClient
public class ApplicationDialogueChatRunner {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(ApplicationDialogueChatRunner.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }

}
