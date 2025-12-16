package com.FinFlow.config.dummy;

import com.FinFlow.domain.User;
import com.FinFlow.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class DummyInit extends DummyObject {

  @Bean
  @Profile("dev")   // prod 모드에서는 실행 X
  CommandLineRunner init(UserRepository userRepository) {
    return (args -> {
      // 서버 실행 시 무조건 실행
      User user = userRepository.save(newUser("test", "test"));
    });
  }
}
