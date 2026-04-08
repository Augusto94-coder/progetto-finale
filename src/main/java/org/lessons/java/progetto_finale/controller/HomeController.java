package org.lessons.java.progetto_finale.controller;

import org.lessons.java.progetto_finale.model.Event;
import org.lessons.java.progetto_finale.service.CategoryService;
import org.lessons.java.progetto_finale.service.EventService;
import org.lessons.java.progetto_finale.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;

@Controller
public class HomeController {

    private final EventService eventService;
    private final UserService userService;
    private final CategoryService categoryService;

    public HomeController(
            EventService eventService,
            UserService userService,
            CategoryService categoryService) {
        this.eventService = eventService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    // PAGINA DASHBOARD/LOGIN

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        model.addAttribute("username", authentication.getName());
        model.addAttribute("roles", authentication.getAuthorities());
        return "dashboard/index";
    }

    // GESTIONE EVENTI

    @GetMapping("/organizer/events")
    public String organizerEvents(Authentication authentication, Model model) {
        model.addAttribute("events", eventService.findVisibleEvents(authentication));
        return "organizer/events/index";
    }

    @GetMapping("/organizer/events/create")
    public String createEventForm(Model model) {
        model.addAttribute("event", new Event());
        model.addAttribute("allCategories", categoryService.findAll());
        model.addAttribute("pageTitle", "Crea evento");
        model.addAttribute("formAction", "/organizer/events/create");
        return "organizer/events/form";
    }

    @PostMapping("/organizer/events/create")
    public String createEvent(
            @Valid Event event,
            BindingResult bindingResult,
            Authentication authentication,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("allCategories", categoryService.findAll());
            model.addAttribute("pageTitle", "Crea evento");
            model.addAttribute("formAction", "/organizer/events/create");
            return "organizer/events/form";
        }

        eventService.createForCurrentUser(event, authentication.getName());
        return "redirect:/organizer/events";
    }

    @GetMapping("/organizer/events/edit/{id}")
    public String editEventForm(@PathVariable Long id, Authentication authentication, Model model) {
        Event event = eventService.getManageableEvent(id, authentication);

        model.addAttribute("event", event);
        model.addAttribute("allCategories", categoryService.findAll());
        model.addAttribute("pageTitle", "Modifica evento");
        model.addAttribute("formAction", "/organizer/events/edit/" + id);

        return "organizer/events/form";
    }

    @PostMapping("/organizer/events/edit/{id}")
    public String editEvent(
            @PathVariable Long id,
            @Valid Event event,
            BindingResult bindingResult,
            Authentication authentication,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("allCategories", categoryService.findAll());
            model.addAttribute("pageTitle", "Modifica evento");
            model.addAttribute("formAction", "/organizer/events/edit/" + id);
            return "organizer/events/form";
        }

        eventService.updateManageableEvent(id, event, authentication);
        return "redirect:/organizer/events";
    }

    @PostMapping("/organizer/events/delete/{id}")
    public String deleteEvent(@PathVariable Long id, Authentication authentication) {
        eventService.deleteManageableEvent(id, authentication);
        return "redirect:/organizer/events";
    }

    // PAGINA ADMIN/UTENTI

    @GetMapping("/admin/users")
    public String adminUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users/index";
    }
}