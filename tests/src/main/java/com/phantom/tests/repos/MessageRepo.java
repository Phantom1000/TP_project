package com.phantom.tests.repos;

import com.phantom.tests.models.Message;
import org.springframework.data.repository.CrudRepository;


public interface MessageRepo extends CrudRepository<Message, Long> {

}
