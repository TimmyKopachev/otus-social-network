package org.otus.dzmitry.kapachou.highload;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.otus.dzmitry.kapachou.highload.model.Person;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
public class SocialNetworkCoreIntegrationTest {

    @BeforeAll
    public static void postgresContainerSetup() {
        SocialNetworkPostgresContainer.getInstance().start();
    }

    @Test
    public void whenSpringContextIsBootstrapped_thenNoExceptions() {
    }

    protected void setupPersonAuthentication(Person authenticated) {
//        Authentication authentication = Mockito.mock(Authentication.class);
//        Mockito.when(authentication.getPrincipal()).thenReturn(new AuthenticatedPersonDetails(authenticated));
//        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
//        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
//        SecurityContextHolder.setContext(securityContext);
    }

}

