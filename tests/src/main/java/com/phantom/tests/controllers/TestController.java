package com.phantom.tests.controllers;

import com.phantom.tests.models.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/")
    public String indexTest(@AuthenticationPrincipal User user, Model model) {
        if (user == null) return "redirect:/registration";
        model.addAttribute("id", user.getId().toString());
        return "index";
    }
}
