package com.FinFlow.config.dummy;

import com.FinFlow.domain.Account;
import com.FinFlow.domain.User;
import com.FinFlow.repository.AccountRepository;
import com.FinFlow.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class DummyInit extends DummyObject {

  @Bean
  @Profile("dev")   // prod 모드에서는 실행 X
  CommandLineRunner init(UserRepository userRepository, AccountRepository accountRepository) {
    return (args -> {
      // 서버 실행 시 무조건 실행
      User test = userRepository.save(newUser("test", "test"));
      User test2 = userRepository.save(newUser("test2", "test2"));
      Account testAccount = accountRepository.save(newAccount("1111111111", test));
      Account test2Account = accountRepository.save(newAccount("2222222222", test2));
    });
  }
}
