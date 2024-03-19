package org.otus.dzmitry.kapachou.highload;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SocialNetworkApplicationRunner {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(SocialNetworkApplicationRunner.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }

}
