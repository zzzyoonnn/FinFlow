package com.FinFlow.config.dummy;

import com.FinFlow.domain.Account;
import com.FinFlow.domain.Transaction;
import com.FinFlow.domain.TransactionEnum;
import com.FinFlow.domain.User;
import com.FinFlow.domain.UserEnum;
import com.FinFlow.repository.AccountRepository;
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

  protected Account newAccount(String number, User user) {
    return Account.builder()
            .number(number)
            .password(1234L)
            .balance(1000L)
            .user(user)
            .build();
  }

  protected Account newMockAccount(Long id, String number, Long balance, User user) {
    return Account.builder()
            .id(id)
            .number(number)
            .password(1234L)
            .balance(balance)
            .user(user)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
  }

  protected Transaction newMockDepositTransaction(Long id, Account account) {
    account.deposit(500L);

    Transaction transaction = Transaction.builder()
            .id(id)
            .withdrawAccount(null)
            .depositAccount(account)
            .withdrawAccountBalance(null)
            .depositAccountBalance(account.getBalance())
            .amount(500L)
            .transaction_type(TransactionEnum.DEPOSIT)
            .sender("ATM")
            .receiver(account.getNumber() + "")
            .tel("010-1111-1111")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    return transaction;
  }

  protected Transaction newDepositTransaction(Account account, AccountRepository accountRepository) {
    account.deposit(100L);

    // 더티체킹이 안됨
    if (accountRepository != null) {
      accountRepository.save(account);
    }

    Transaction transaction = Transaction.builder()
            .withdrawAccount(null)
            .depositAccount(account)
            .withdrawAccountBalance(null)
            .depositAccountBalance(account.getBalance())
            .amount(100L)
            .transaction_type(TransactionEnum.DEPOSIT)
            .sender("ATM")
            .receiver(account.getNumber() + "")
            .tel("01012345678")
            .build();

    return transaction;
  }

  protected Transaction newWithdrawTransaction(Account account, AccountRepository accountRepository) {
    account.withdraw(100L);

    // 더티체킹이 안됨
    if (accountRepository != null) {
      accountRepository.save(account);
    }

    Transaction transaction = Transaction.builder()
            .withdrawAccount(account)
            .depositAccount(null)
            .withdrawAccountBalance(account.getBalance())
            .depositAccountBalance(null)
            .amount(100L)
            .transaction_type(TransactionEnum.WITHDRAW)
            .sender(account.getNumber() + "")
            .receiver("ATM")
            .build();

    return transaction;
  }
  protected Transaction newTransferTransaction(Account withdrawAccount, Account depositAccount, AccountRepository accountRepository) {
    withdrawAccount.withdraw(100L);
    depositAccount.deposit(100L);

    // 더티체킹이 안됨
    if (accountRepository != null) {
      accountRepository.save(withdrawAccount);
      accountRepository.save(depositAccount);
    }

    Transaction transaction = Transaction.builder()
            .withdrawAccount(withdrawAccount)
            .depositAccount(depositAccount)
            .withdrawAccountBalance(withdrawAccount.getBalance())
            .depositAccountBalance(depositAccount.getBalance())
            .amount(100L)
            .transaction_type(TransactionEnum.TRANSFER)
            .sender(withdrawAccount.getNumber() + "")
            .receiver(depositAccount.getNumber() + "")
            .build();

    return transaction;
  }
}
