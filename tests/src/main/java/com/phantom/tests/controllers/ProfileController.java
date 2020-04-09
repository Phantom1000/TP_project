package com.phantom.tests.controllers;

import com.phantom.tests.models.User;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProfileController {

    @GetMapping("profile/{id}")
    public String userEditForm(@AuthenticationPrincipal User currentUser, @PathVariable(name = "id") User user, Model model) {
        if (currentUser.equals(user)) {
            model.addAttribute("user",
                    String.format("%s %s %s", user.getSurname(), user.getFirstname(), user.getPatronymic()));

            return "profile";
        }
        return "redirect:/";
    }
}
