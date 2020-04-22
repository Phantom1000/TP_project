package com.phantom.tests.controllers;


import com.phantom.tests.models.Role;
import com.phantom.tests.models.User;
import com.phantom.tests.services.MessageService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/message")
public class MessageController {
    private final MessageService messageService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("")
    public String send(@RequestParam String text, @RequestParam(name = "recipient") User user) {
        messageService.addMessage(text, user);
        return "redirect:/";
    }

    @GetMapping("/{user}")
    public String index(@AuthenticationPrincipal User currentUser, @PathVariable User user, Model model) {
        if (user != null) {
            boolean isAdmin = user.getRoles().contains(Role.ADMIN);
            if (currentUser.equals(user)) {
                if (isAdmin) {
                    model.addAttribute("messages", messageService.findAll());
                } else {
                    model.addAttribute("messages", user.getMessages());
                    if (!currentUser.getRoles().contains(Role.ADMIN)) {
                        user.getMessages().stream().forEach(m -> {
                            messageService.viewMessage(m);
                        });
                    }
                }
                model.addAttribute("id", currentUser.getId());
                model.addAttribute("isAdmin", isAdmin);
            } else if (currentUser.getRoles().contains(Role.ADMIN)) {
                if (isAdmin) {
                    model.addAttribute("messages", messageService.findAll());
                } else {
                    model.addAttribute("messages", user.getMessages());
                }
                model.addAttribute("id", currentUser.getId());
                model.addAttribute("isAdmin", isAdmin);
            } else {
                return "redirect:/";
            }
        } else {
            return "redirect:/";
        }

        return "messages";
    }

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }
}
