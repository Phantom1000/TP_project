package com.phantom.tests.services;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.phantom.tests.models.Result;
import com.phantom.tests.models.Answer;
import com.phantom.tests.models.Position;
import com.phantom.tests.models.Test;
import com.phantom.tests.models.User;
import com.phantom.tests.repos.AnswerRepo;
import com.phantom.tests.repos.ResultRepo;
import com.phantom.tests.repos.TestRepo;

import org.springframework.stereotype.Service;

@Service
public class TestService {
    private final TestRepo testRepo;
    private final AnswerRepo answerRepo;
    private final ResultRepo resultRepo;
    
    public Test getRandomTestByPosition(Position position) {
        Random rnd = new Random();
        List<Test> tests = testRepo.findByPosition(position);
        Test test = tests.get(rnd.nextInt(tests.size()));
        return test;
    }

    public Result getResult(Map<String, String> form, User user) {
        form.remove("_csrf");
        Set<Answer> answers = new HashSet<>();
        for(Map.Entry<String, String> item : form.entrySet()) {
            answers.add(answerRepo.findById(Long.parseLong(item.getValue())).orElseThrow());
        }
        float rating = answers.stream().filter(ans -> ans.getCorrect()).count() / (float)answers.size();
        Result res = new Result(answers, user, rating);
        resultRepo.save(res);
        return res;
    }

    public TestService(TestRepo testRepo, AnswerRepo answerRepo, ResultRepo resultRepo) {
        this.testRepo = testRepo;
        this.answerRepo = answerRepo;
        this.resultRepo = resultRepo;
    }
}