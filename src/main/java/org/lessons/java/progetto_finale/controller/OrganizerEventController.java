package org.lessons.java.progetto_finale.controller;

import org.lessons.java.progetto_finale.model.Event;
import org.lessons.java.progetto_finale.service.CategoryService;
import org.lessons.java.progetto_finale.service.EventService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/organizer/events")
public class OrganizerEventController {

    private static final String EVENTS_INDEX_VIEW = "organizer/events/index";

    private static final String EVENT_FORM_VIEW = "organizer/events/form";

    private static final String EVENTS_REDIRECT = "redirect:/organizer/events";

    private final EventService eventService;
    private final CategoryService categoryService;

    public OrganizerEventController(
            EventService eventService,
            CategoryService categoryService) {

        this.eventService = eventService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String index(
            Authentication authentication,
            Model model) {

        model.addAttribute(
                "events",
                eventService.findVisibleEvents(authentication));

        return EVENTS_INDEX_VIEW;
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        prepareForm(
                model,
                new Event(),
                "Crea evento",
                "/organizer/events/create");

        return EVENT_FORM_VIEW;
    }

    @PostMapping("/create")
    public String create(
            @Valid @ModelAttribute("event") Event event,
            BindingResult bindingResult,
            Authentication authentication,
            Model model) {

        if (bindingResult.hasErrors()) {
            prepareForm(
                    model,
                    event,
                    "Crea evento",
                    "/organizer/events/create");

            return EVENT_FORM_VIEW;
        }

        eventService.createForCurrentUser(
                event,
                authentication.getName());

        return EVENTS_REDIRECT;
    }

    @GetMapping("/{id}/edit")
    public String editForm(
            @PathVariable Long id,
            Authentication authentication,
            Model model) {

        Event event = eventService.getManageableEvent(
                id,
                authentication);

        prepareForm(
                model,
                event,
                "Modifica evento",
                "/organizer/events/" + id + "/edit");

        return EVENT_FORM_VIEW;
    }

    @PostMapping("/{id}/edit")
    public String edit(
            @PathVariable Long id,
            @Valid @ModelAttribute("event") Event event,
            BindingResult bindingResult,
            Authentication authentication,
            Model model) {

        if (bindingResult.hasErrors()) {
            prepareForm(
                    model,
                    event,
                    "Modifica evento",
                    "/organizer/events/" + id + "/edit");

            return EVENT_FORM_VIEW;
        }

        eventService.updateManageableEvent(
                id,
                event,
                authentication);

        return EVENTS_REDIRECT;
    }

    @PostMapping("/{id}/delete")
    public String delete(
            @PathVariable Long id,
            Authentication authentication) {

        eventService.deleteManageableEvent(id, authentication);

        return EVENTS_REDIRECT;
    }

    private void prepareForm(
            Model model,
            Event event,
            String pageTitle,
            String formAction) {

        model.addAttribute("event", event);
        model.addAttribute(
                "allCategories",
                categoryService.findAll());
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("formAction", formAction);
    }
}