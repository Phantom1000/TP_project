package com.phantom.tests.controllers;

import com.phantom.tests.models.User;
import com.phantom.tests.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProfileController {
    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("profile/{id}")
    public String userEditForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", String.format("%s %s %s", user.getSurname(), user.getLastname(), user.getPatronymic()));
        return "profile";
    }
}
