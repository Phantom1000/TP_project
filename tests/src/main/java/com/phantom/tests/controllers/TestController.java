package com.phantom.tests.controllers;

import java.util.Map;

import com.phantom.tests.models.*;
import com.phantom.tests.services.TestService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


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
        model.addAttribute("result", res.getId());
        model.addAttribute("id", user.getId());
        return "result";
    }

    @GetMapping("/analysis/{result}")
    public String analysisTest(@AuthenticationPrincipal User user, @PathVariable(name = "result") Result result, Model model) {
        if (user.equals(result.getUser()) || user.getAuthorities().contains(Role.ADMIN)) {
            model.addAttribute("answers", result.getAnswers());
            model.addAttribute("test", testService.getTestByResult(result));
            model.addAttribute("id", user.getId());
            return "analysis";
        }
        return "redirect:/";
    }
    
}
