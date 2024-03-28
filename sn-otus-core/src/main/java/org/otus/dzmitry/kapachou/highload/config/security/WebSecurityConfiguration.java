package org.otus.dzmitry.kapachou.highload.config.security;

import lombok.RequiredArgsConstructor;
import org.otus.dzmitry.kapachou.highload.service.PersonService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.session.hazelcast.HazelcastIndexedSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {

    private final PersonService personService;
    private final JwtRequestFilter jwtRequestFilter;
    final HazelcastIndexedSessionRepository hzSessionRepository;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authenticationProvider())
                .authenticationEventPublisher(authenticationEventPublisher())
                .build();
    }

    @Bean
    @Lazy
    DaoAuthenticationProvider authenticationProvider() {
        var authenticationProvider = new DaoAuthenticationProvider(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService());
        return authenticationProvider;
    }

    @Bean
    protected UserDetailsService userDetailsService() {
        return this.personService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    AuthenticationEventPublisher authenticationEventPublisher() {
        return new DefaultAuthenticationEventPublisher();
    }

    @Bean
    public StrictHttpFirewall httpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowedHttpMethods(Arrays.asList(HttpMethod.GET.name(), HttpMethod.POST.name()));
        firewall.setAllowSemicolon(true);
        firewall.setUnsafeAllowAnyHttpMethod(true);
        return firewall;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers(HttpMethod.POST, "/sign-in", "/sign-up").permitAll();
                    registry.requestMatchers(HttpMethod.GET, "/actuator/**").permitAll();
                    registry.anyRequest().authenticated();
                })
                .headers(headerCustomizer -> headerCustomizer
                        .defaultsDisabled()
                        .cacheControl(withDefaults()))
                .sessionManagement(sessionManager -> sessionManager.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                //.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(httpSessionConfigurer -> httpSessionConfigurer
                        .maximumSessions(2)
                        .sessionRegistry(hzSessionRepository()))
                //.authenticationProvider(authenticationProvider())
                .formLogin(AbstractHttpConfigurer::disable)
                //.httpBasic(AbstractHttpConfigurer::disable);
                .httpBasic(withDefaults())
                .formLogin(withDefaults());

        return http.build();
    }

    @Bean
    public SpringSessionBackedSessionRegistry<?> hzSessionRepository() {
        return new SpringSessionBackedSessionRegistry<>(hzSessionRepository);
    }


}
