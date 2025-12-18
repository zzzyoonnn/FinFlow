package com.FinFlow.repository;

import com.FinFlow.domain.Account;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

  // select * from account where number = :number
  Optional<Account> findByNumber(String number);

  // select * from account where user_id = :id
  List<Account> findByUserId(Long id);

}
