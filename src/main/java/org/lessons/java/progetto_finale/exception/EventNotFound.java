package org.lessons.java.progetto_finale.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EventNotFound extends RuntimeException {
    public EventNotFound(String message) {
        super(message);
    }
}
