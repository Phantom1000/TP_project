package com.phantom.tests.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProfileController {
    @GetMapping("profile/{user}")
    public String userEditForm(@PathVariable String user, Model model) {
        model.addAttribute("user", "Иванов Иван Иванович");
        return "profile";
    }
}
