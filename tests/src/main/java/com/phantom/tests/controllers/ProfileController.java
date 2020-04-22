package com.phantom.tests.controllers;

import com.phantom.tests.models.Role;
import com.phantom.tests.models.User;

import com.phantom.tests.services.TestService;
import com.phantom.tests.services.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final UserService userService;
    private final TestService testService;
    private final PasswordEncoder passwordEncoder;

    public ProfileController(UserService userService, TestService testService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.testService = testService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/{id}")
    public String showProfile(@AuthenticationPrincipal User currentUser, @PathVariable(name = "id") User user, Model model) {
        if (user != null) {
            if (currentUser.equals(user) || currentUser.getAuthorities().contains(Role.ADMIN)) {
                model.addAttribute("user",
                        String.format("%s %s %s", user.getSurname(), user.getFirstname(), user.getPatronymic()));
                model.addAttribute("id", user.getId());
                model.addAttribute("results", user.getResults());
                model.addAttribute("isView", !(user.getMessages().stream().filter(m -> !m.isView()).count() > 0));
                List<String> jobs = new ArrayList<>();
                user.getResults().stream().forEach(r -> jobs.add(testService.getTestByResult(r).getPosition().toString()));
                model.addAttribute("jobs", jobs);
                return "profile";
            }
        }
        return "redirect:/";
    }

    @GetMapping("/{id}/update")
    public String editProfile(@AuthenticationPrincipal User currentUser, @PathVariable(name = "id") User user, Model model) {
        if (user != null) {
            if (currentUser.equals(user) || currentUser.getAuthorities().contains(Role.ADMIN)) {
                model.addAttribute("editUser", user);
                model.addAttribute("id", currentUser.getId());
                return "updateProfile";
            }
        }
        return "redirect:/";
    }

    @PostMapping("/{id}/update")
    public String updateProfile(@AuthenticationPrincipal User currentUser,
                                @Valid User user, BindingResult bindingResult,
                                @PathVariable(name = "id") User editUser, Model model) {
        if (currentUser.equals(editUser) || currentUser.getAuthorities().contains(Role.ADMIN)) {
            if (bindingResult.hasFieldErrors("surname") || bindingResult.hasFieldErrors("firstname") || bindingResult.hasFieldErrors("patronymic")) {
                model.addAttribute("editUser", user);
                return "updateProfile";
            }
            userService.updateUser(editUser, user);
            return "redirect:/profile/" + editUser.getId();
        }
        return "redirect:/";
    }

}
