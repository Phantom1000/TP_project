package com.phantom.tests.controllers;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.phantom.tests.models.Position;
import com.phantom.tests.models.Result;
import com.phantom.tests.models.Test;
import com.phantom.tests.models.User;
import com.phantom.tests.services.TestService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class TestController {

    private final TestService testService;

    @GetMapping("/")
    public String indexPosition(@AuthenticationPrincipal User user, Model model) {
        if (user == null)
            return "redirect:/registration";
        model.addAttribute("id", user.getId());
        model.addAttribute("positions", Position.values());
        return "index";
    }

    @PostMapping("/test")
    public String indexTest(@AuthenticationPrincipal User user, @RequestParam Map<String, String> form, Model model) {
        Position position = Position.valueOf(form.get("position"));
        Test test = testService.getRandomTestByPosition(position);
        model.addAttribute("test", test);
        model.addAttribute("position", position);
        model.addAttribute("id", user.getId());
        return "test";
    }

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @PostMapping("/record")
    public String recordTest(@AuthenticationPrincipal User user, @RequestParam Map<String, String> form, Model model) {
        Result res = testService.getResult(form, user);
        model.addAttribute("rating", (int)(res.getRating() * 100));
        return "result";
    }
    
}
