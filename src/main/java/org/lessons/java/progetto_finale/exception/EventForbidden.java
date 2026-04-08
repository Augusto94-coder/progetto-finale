package org.lessons.java.progetto_finale.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class EventForbidden extends RuntimeException {
    public EventForbidden(String message) {
        super(message);
    }
}
