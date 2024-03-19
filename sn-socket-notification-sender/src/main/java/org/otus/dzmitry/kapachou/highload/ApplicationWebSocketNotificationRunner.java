package org.otus.dzmitry.kapachou.highload;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ApplicationWebSocketNotificationRunner {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(ApplicationWebSocketNotificationRunner.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }

}
