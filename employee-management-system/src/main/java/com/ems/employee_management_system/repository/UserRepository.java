package com.ems.employee_management_system.repository;

import com.ems.employee_management_system.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByUsername(String username);

    // findByEmail returns List to handle duplicate entries safely
    List<User> findByEmail(String email);

    List<User> findByRole(String role);
}
