package com.phantom.tests.services;

import java.util.List;
import java.util.Random;

import com.phantom.tests.models.Position;
import com.phantom.tests.models.Test;
import com.phantom.tests.repos.TestRepo;

import org.springframework.stereotype.Service;

@Service
public class TestService {
    private final TestRepo testRepo;
    
    public Test getRandomTestByPosition(Position position) {
        Random rnd = new Random();
        List<Test> tests = testRepo.findByPosition(position);
        Test test = tests.get(rnd.nextInt(tests.size()));
        return test;
    }

    public TestService(TestRepo testRepo) {
        this.testRepo = testRepo;
    }
}