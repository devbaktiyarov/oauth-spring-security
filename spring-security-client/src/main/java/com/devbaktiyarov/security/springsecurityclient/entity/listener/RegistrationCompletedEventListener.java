package com.devbaktiyarov.security.springsecurityclient.entity.listener;

import com.devbaktiyarov.security.springsecurityclient.entity.User;
import com.devbaktiyarov.security.springsecurityclient.event.RegistrationCompetedEvent;
import org.springframework.context.ApplicationListener;

import java.util.UUID;

public class RegistrationCompletedEventListener implements ApplicationListener<RegistrationCompetedEvent> {
    @Override
    public void onApplicationEvent(RegistrationCompetedEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
    }
}
