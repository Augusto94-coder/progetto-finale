package org.lessons.java.progetto_finale.controller;

import org.lessons.java.progetto_finale.model.Event;
import org.lessons.java.progetto_finale.service.EventService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/events")
@CrossOrigin(origins = "http://localhost:5173")
public class PublicEventController {

    private final EventService eventService;

    public PublicEventController(EventService eventService) {
        this.eventService = eventService;
    }


    @GetMapping
    public List<Event> getAllEvents() {
        return eventService.findAll();
    }

    @GetMapping("/{id}")
    public Event getById(@PathVariable Long id) {
        return eventService.findById(id);
    }
}