package org.otus.dzmitry.kapachou.highload.config;

import com.hazelcast.config.SecurityConfig;
import org.apache.catalina.util.SessionConfig;
import org.otus.dzmitry.kapachou.highload.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

@Configuration
public class ApplicationConfiguration {


    public static class SecurityInitializer extends AbstractSecurityWebApplicationInitializer {
        public SecurityInitializer() {
            super(SecurityConfig.class, SessionConfig.class);
        }
    }

    public static class HzSessionInitializer extends AbstractHttpSessionApplicationInitializer {

    }

}
