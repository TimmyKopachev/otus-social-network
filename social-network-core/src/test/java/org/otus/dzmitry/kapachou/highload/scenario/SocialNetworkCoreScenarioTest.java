package org.otus.dzmitry.kapachou.highload.scenario;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.otus.dzmitry.kapachou.highload.SocialNetworkCoreIntegrationTest;
import org.otus.dzmitry.kapachou.highload.model.Person;
import org.otus.dzmitry.kapachou.highload.model.auth.AuthPerson;
import org.otus.dzmitry.kapachou.highload.service.FriendService;
import org.otus.dzmitry.kapachou.highload.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;


public class SocialNetworkCoreScenarioTest extends SocialNetworkCoreIntegrationTest {

    @Autowired
    FriendService friendService;



}
