package com.devbaktiyarov.security.springsecurityclient.event;

import com.devbaktiyarov.security.springsecurityclient.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationCompetedEvent extends ApplicationEvent {

    private User user;
    private String applicationUrl;

    public RegistrationCompetedEvent(User user, String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }
}
