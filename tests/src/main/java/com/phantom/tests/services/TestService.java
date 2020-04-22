package com.phantom.tests.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.phantom.tests.models.*;
import com.phantom.tests.repos.AnswerRepo;
import com.phantom.tests.repos.QuestionRepo;
import com.phantom.tests.repos.ResultRepo;
import com.phantom.tests.repos.TestRepo;

import org.springframework.stereotype.Service;

@Service
public class TestService {
    private final int QUESTION_COUNT = 4;
    private final int ANSWER_COUNT = 4;

    private final TestRepo testRepo;
    private final AnswerRepo answerRepo;
    private final QuestionRepo questionRepo;
    private final ResultRepo resultRepo;

    public Test getTest(Long id) {
        return testRepo.findById(id).orElseThrow();
    }

    public Iterable<Test> findAll() {
        return testRepo.findAll();
    }

    public Test getTestByResult(Result result) {
        Answer answer = result.getAnswers().get(0);
        Question question = answer.getQuestion();
        return question.getTest();
    }

    public Test getRandomTestByPosition(Position position) {
        Random rnd = new Random();
        List<Test> tests = testRepo.findByPosition(position);
        Test test = tests.get(rnd.nextInt(tests.size()));
        //Collections.shuffle(test.getQuestions());
        //test.getQuestions().stream().forEach(a -> Collections.shuffle(a.getAnswers()));
        return test;
    }

    public Result getResult(Map<String, String> form, User user) {
        form.remove("_csrf");
        List<Answer> answers = new ArrayList<>();
        for (Map.Entry<String, String> item : form.entrySet()) {
            answers.add(answerRepo.findById(Long.parseLong(item.getValue())).orElseThrow());
        }
        float rating = answers.stream().filter(ans -> ans.isCorrect()).count() / (float) answers.size();
        Result res = new Result(answers, user, rating);
        resultRepo.save(res);
        return res;
    }

    public void updateTest(Map<String, String> form, Test test) {
        List<Question> questions = test.getQuestions();
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            String questParam = form.get("questions" + Integer.toString(i));
            if (questParam != null && !questParam.equals(question.getText()))
                question.setText(questParam);
            Long correctAns = Long.parseLong(form.get("correct" + Integer.toString(i)));
            List<Answer> answers = questions.get(i).getAnswers();
            for (int j = 0; j < answers.size(); j++) {
                Answer answer = answers.get(j);
                String answerParam = form.get("answers" + Integer.toString(i * questions.size() + j));
                if (answerParam != null && !answerParam.equals(answer.getText()))
                    answer.setText(answerParam);
                if (answer.getId() == correctAns) {
                    answer.setCorrect(true);
                } else {
                    answer.setCorrect(false);
                }
            }
        }
        testRepo.save(test);
    }

    public void createTest(Map<String, String> form) {
        Test test = new Test(Position.valueOf(form.get("position")));
        testRepo.save(test);
        for (int i = 0; i < QUESTION_COUNT; i++) {
            String questParam = form.get("questions" + Integer.toString(i));
            Question question = new Question(questParam, test);
            questionRepo.save(question);
            Long correctAns = Long.parseLong(form.get("correct" + Integer.toString(i)));
            for (int j = 0; j < ANSWER_COUNT; j++) {
                String answerParam = form.get("answers" + Integer.toString(i * QUESTION_COUNT + j));
                Answer answer = new Answer(answerParam, j == correctAns, question);
                answerRepo.save(answer);
            }
        }
    }

    public void deleteTest(Test test) {
        testRepo.delete(test);
    }

    public TestService(TestRepo testRepo, AnswerRepo answerRepo, ResultRepo resultRepo, QuestionRepo questionRepo) {
        this.testRepo = testRepo;
        this.answerRepo = answerRepo;
        this.resultRepo = resultRepo;
        this.questionRepo = questionRepo;
    }
}