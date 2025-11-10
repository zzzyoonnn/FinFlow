package com.FinFlow.config.dummy;

import com.FinFlow.domain.User;
import com.FinFlow.domain.UserEnum;
import java.time.LocalDateTime;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class DummyObject {
  protected User newUser(String username, String fullname) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    String encPassword = encoder.encode("1234");

    return User.builder()
            .username(username)
            .password(encPassword)
            .email(username + "@test.com")
            .fullname(fullname)
            .role(UserEnum.CUSTOMER)
            .build();
  }

  protected User newMockUser(Long id, String username, String fullname) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    String encPassword = encoder.encode("1234");

    return User.builder()
            .id(id)
            .username(username)
            .password(encPassword)
            .email(username + "@test.com")
            .fullname(fullname)
            .role(UserEnum.CUSTOMER)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
  }
}
