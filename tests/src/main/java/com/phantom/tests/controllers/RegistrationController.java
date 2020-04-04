package com.phantom.tests.controllers;

import com.phantom.tests.models.User;
import com.phantom.tests.services.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class RegistrationController {
    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            model.addAttribute("user", user);
            return "registration";
        }

        boolean userExists = !userService.addUser(user);
        model.addAttribute("userExists", userExists);
        if (userExists) {
            return "registration";
        }

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(
            new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    user.getAuthorities()));
        return "redirect:/";
    }
}
