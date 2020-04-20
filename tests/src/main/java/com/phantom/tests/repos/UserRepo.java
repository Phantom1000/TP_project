package com.phantom.tests.repos;

import com.phantom.tests.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Query("select u from #{#entityName} u where u.username like %:pattern%")
    List<User> search(@Param("pattern") String pattern);
}
