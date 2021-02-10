package com.dorka.fifaapp.repo;

import com.dorka.fifaapp.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByName(String name);
}
