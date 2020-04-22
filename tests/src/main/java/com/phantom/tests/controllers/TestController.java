package com.phantom.tests.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.phantom.tests.models.*;
import com.phantom.tests.services.TestService;

import com.phantom.tests.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final UserService userService;

    @GetMapping("/")
    public String indexPosition(@AuthenticationPrincipal User user, @RequestParam(required = false) String show,
                                @RequestParam(required = false) String search, Model model) {
        if (user == null)
            return "redirect:/registration";
        if (user.getRoles().contains(Role.ADMIN)) {
            model.addAttribute("id", user.getId());
            if (show != null && show.equals("tests")) {
                model.addAttribute("tests", testService.findAll());
                model.addAttribute("show", "tests");
            } else {
                if (search != null) {
                    model.addAttribute("users", userService.search(search));
                } else {
                    model.addAttribute("users", userService.findAll());
                }
                model.addAttribute("show", "users");
            }

            return "admin";
        }
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

    public TestController(TestService testService, UserService userService) {
        this.testService = testService;
        this.userService = userService;
    }

    @PostMapping("/record")
    public String recordTest(@AuthenticationPrincipal User user, @RequestParam Test test, @RequestParam Map<String, String> form, Model model) {
        Result res = testService.getResult(form, user, test);
        model.addAttribute("rating", (int) (res.getRating() * 100));
        model.addAttribute("result", res.getId());
        model.addAttribute("id", user.getId());
        return "result";
    }

    @GetMapping("/analysis/{result}")
    public String analysisTest(@AuthenticationPrincipal User user, @PathVariable(name = "result") Result result, Model model) {
        if (result != null) {
            if (user.equals(result.getUser()) || user.getAuthorities().contains(Role.ADMIN)) {
                model.addAttribute("answers", result.getAnswers());
                model.addAttribute("test", result.getTest());
                List<Boolean> correct = new ArrayList<>();
                List<String> colors = new ArrayList<>();
                List<Question> questions = result.getTest().getQuestions();
                for (Question question : questions) {
                    for (Answer answer : question.getAnswers()) {
                        String color = "";
                        if (answer.isCorrect()) {
                            color = "text-success";
                        }
                        for (Answer answer2 : result.getAnswers()) {
                            if (question.equals(answer2.getQuestion())) {
                                if (answer.equals(answer2)) {
                                    color = "text-primary";
                                }
                            }

                        }
                        colors.add(color);
                    }
                }
                outer:
                for (Question question : questions) {
                    for (Answer answer : result.getAnswers()) {
                        if (answer.getQuestion() == null) {
                            correct.add(false);
                            continue outer;
                        } else if (question.equals(answer.getQuestion())) {
                            correct.add(answer.isCorrect());
                            continue outer;
                        }
                    }
                }
                model.addAttribute("correct", correct);
                model.addAttribute("colors", colors);
                model.addAttribute("id", user.getId());
                return "analysis";
            }
        }
        return "redirect:/";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/test/{id}")
    public String showTest(@AuthenticationPrincipal User user, @PathVariable(name = "id") Test test, Model model) {
        if (test != null) {
            model.addAttribute("test", test);
            model.addAttribute("id", user.getId());
            return "showTest";
        }
        return "redirect:/?show=tests";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/test/{id}/update")
    public String editTest(@AuthenticationPrincipal User user, @PathVariable(name = "id") Test test, Model model) {
        if (test != null) {
            model.addAttribute("test", test);
            model.addAttribute("errors", new ArrayList<String>());
            model.addAttribute("id", user.getId());
            return "updateTest";
        }
        return "redirect:/?show=tests";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/test/{id}/update")
    public String updateTest(@AuthenticationPrincipal User user, @PathVariable(name = "id") Test test,
                             @RequestParam Map<String, String> form,
                             Model model) {
        if (test != null) {
            testService.updateTest(form, test);
        }
        return "redirect:/?show=tests";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/test/create")
    public String createTest(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("positions", Position.values());
        model.addAttribute("id", user.getId());
        return "createTest";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/test/store")
    public String storeTest(@AuthenticationPrincipal User user, @RequestParam Map<String, String> form, Model model) {
        testService.createTest(form);
        return "redirect:/?show=tests";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/test/{id}/delete")
    public String destroyTest(@AuthenticationPrincipal User user, @PathVariable(name = "id") Test test, Model model) {
        testService.deleteTest(test);
        return "redirect:/?show=tests";
    }
}
