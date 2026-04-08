package org.lessons.java.progetto_finale.service;

import java.util.List;

import org.lessons.java.progetto_finale.exception.EventForbidden;
import org.lessons.java.progetto_finale.exception.EventNotFound;
import org.lessons.java.progetto_finale.model.Event;
import org.lessons.java.progetto_finale.model.User;
import org.lessons.java.progetto_finale.repository.EventRepository;
import org.lessons.java.progetto_finale.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public EventService(
            EventRepository eventRepository,
            UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    public Event findById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFound("Evento non trovato"));
    }

    public List<Event> findByCreatorUsername(String username) {
        return eventRepository.findByCreatedByUserUsername(username);
    }

    public boolean isAdmin(Authentication authentication) {
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if ("ROLE_ADMIN".equals(authority.getAuthority())) {
                return true;
            }
        }
        return false;
    }

    public List<Event> findVisibleEvents(Authentication authentication) {
        if (isAdmin(authentication)) {
            return findAll();
        }
        return findByCreatorUsername(authentication.getName());
    }

    public Event getManageableEvent(Long id, Authentication authentication) {
        Event event = findById(id);

        if (isAdmin(authentication)) {
            return event;
        }

        if (event.getCreatedByUser() != null
                && event.getCreatedByUser().getUsername().equals(authentication.getName())) {
            return event;
        }

        throw new EventForbidden("Non sei autorizzato a gestire questo evento");
    }

    public Event createForCurrentUser(Event event, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EventNotFound("Utente non trovato"));

        event.setId(null);
        event.setCreatedByUser(user);

        return eventRepository.save(event);
    }

    public Event updateManageableEvent(Long id, Event updatedEvent, Authentication authentication) {
        Event event = getManageableEvent(id, authentication);

        event.setTitle(updatedEvent.getTitle());
        event.setDescription(updatedEvent.getDescription());
        event.setCity(updatedEvent.getCity());
        event.setAddress(updatedEvent.getAddress());
        event.setStartAt(updatedEvent.getStartAt());
        event.setEndAt(updatedEvent.getEndAt());
        event.setCategories(updatedEvent.getCategories());

        return eventRepository.save(event);
    }

    @Transactional
    public void deleteManageableEvent(Long id, Authentication authentication) {
        Event event = getManageableEvent(id, authentication);

        event.getCategories().clear();
        eventRepository.save(event);

        eventRepository.delete(event);
    }
}