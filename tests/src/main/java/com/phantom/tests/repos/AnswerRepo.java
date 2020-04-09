package com.phantom.tests.repos;

import java.util.Optional;

import com.phantom.tests.models.Answer;

import org.springframework.data.repository.CrudRepository;

public interface AnswerRepo extends CrudRepository<Answer, Long> {
    Optional<Answer> findById(Long id);
}