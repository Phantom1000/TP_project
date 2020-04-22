package com.phantom.tests.repos;

import com.phantom.tests.models.Question;
import org.springframework.data.repository.CrudRepository;


public interface QuestionRepo extends CrudRepository<Question, Long> {

}
