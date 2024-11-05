package com.API.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;


@Getter @Setter
public class DentistUnavailableEvent extends ApplicationEvent {

    private Long dentistId;
    private String reason;

    public DentistUnavailableEvent(Object source, Long dentistId, String reason) {
        super(source);
        this.dentistId = dentistId;
        this.reason = reason;
    }
}
