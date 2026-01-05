package com.FinFlow.domain.transaction;

import com.FinFlow.config.dummy.DummyObject;
import com.FinFlow.domain.Account;
import com.FinFlow.domain.Transaction;
import com.FinFlow.domain.User;
import com.FinFlow.repository.AccountRepository;
import com.FinFlow.repository.TransactionRepository;
import com.FinFlow.repository.UserRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
public class TransactionRepositoryImplTests extends DummyObject {

  @Autowired
  private TransactionRepository transactionRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private EntityManager entityManager;

  @BeforeEach
  public void setUp() {
    autoIncrementReset();
    dataSetting();
  }

  private void dataSetting() {
    User Alice = userRepository.save(newUser("Alice", "Alice"));
    User Bob = userRepository.save(newUser("Bob", "Bob"));
    User Charlie = userRepository.save(newUser("Charlie", "Charlie"));
    User David = userRepository.save(newUser("David", "David"));

    Account alicesAccount = accountRepository.save(newAccount("1111111111", Alice));
    Account bobAccount = accountRepository.save(newAccount("2222222222", Bob));
    Account charlieAccount = accountRepository.save(newAccount("3333333333", Charlie));
    Account davidAccount = accountRepository.save(newAccount("4444444444", David));

    Transaction withdrawTransaction1 = transactionRepository
            .save(newWithdrawTransaction(alicesAccount, accountRepository));
    Transaction depositTransaction1 = transactionRepository
            .save(newDepositTransaction(bobAccount, accountRepository));
    Transaction transferTransaction1 = transactionRepository
            .save(newTransferTransaction(alicesAccount, bobAccount, accountRepository));
    Transaction transferTransaction2 = transactionRepository
            .save(newTransferTransaction(alicesAccount, charlieAccount, accountRepository));
    Transaction transferTransaction3 = transactionRepository
            .save(newTransferTransaction(bobAccount, alicesAccount, accountRepository));
  }

@Test
public void dataJpa_test1() {
  List<Transaction> transactionList = transactionRepository.findAll();
  transactionList.forEach(transaction -> {
    System.out.println("No." + transaction.getId());
    System.out.println("fromAccount: " + transaction.getSender());
    System.out.println("toAccount: " + transaction.getReceiver());
    System.out.println("Type: " + transaction.getTransaction_type());
    System.out.println("-------------------------------");
  });
}

@Test
public void dataJpa_test2() {
  List<Transaction> transactionList = transactionRepository.findAll();
  transactionList.forEach(transaction -> {
    System.out.println("No." + transaction.getId());
    System.out.println("fromAccount: " + transaction.getSender());
    System.out.println("toAccount: " + transaction.getReceiver());
    System.out.println("Type: " + transaction.getTransaction_type());
    System.out.println("-------------------------------");
  });
}

  private void autoIncrementReset() {
    entityManager.createNativeQuery("ALTER TABLE users ALTER COLUMN id RESTART WITH 1;").executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE account ALTER COLUMN id RESTART WITH 1;").executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE account_transaction ALTER COLUMN id RESTART WITH 1;").executeUpdate();
  }
}
