package com.phantom.tests.repos;

import java.util.List;

import com.phantom.tests.models.Position;
import com.phantom.tests.models.Test;

import org.springframework.data.repository.CrudRepository;

public interface TestRepo extends CrudRepository<Test, Long> {
    List<Test> findByPosition(Position position);
}