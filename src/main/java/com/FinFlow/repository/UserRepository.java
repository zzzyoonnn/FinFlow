package com.FinFlow.repository;

import com.FinFlow.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  // select * from user where username = ?
  Optional<User> findByUsername(String username); // Jpa NameQuery 작동

}
