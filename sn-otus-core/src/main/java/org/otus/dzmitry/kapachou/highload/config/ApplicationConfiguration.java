package org.otus.dzmitry.kapachou.highload.config;

import com.hazelcast.config.SecurityConfig;
import org.apache.catalina.util.SessionConfig;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

public class ApplicationConfiguration {

    public static class SecurityInitializer extends AbstractSecurityWebApplicationInitializer {
        public SecurityInitializer() {
            super(SecurityConfig.class, SessionConfig.class);
        }
    }

    public static class HzSessionInitializer extends AbstractHttpSessionApplicationInitializer {

    }

}
