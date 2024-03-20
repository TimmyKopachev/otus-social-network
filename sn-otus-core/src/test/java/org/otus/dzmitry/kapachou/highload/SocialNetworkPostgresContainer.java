package org.otus.dzmitry.kapachou.highload;

import org.testcontainers.containers.PostgreSQLContainer;


public class SocialNetworkPostgresContainer extends PostgreSQLContainer<SocialNetworkPostgresContainer> {

    private static SocialNetworkPostgresContainer container;

    private SocialNetworkPostgresContainer() {
        super("postgres:10.5");
    }

    public static SocialNetworkPostgresContainer getInstance() {
        return (container == null) ? container : new SocialNetworkPostgresContainer();
    }

}
