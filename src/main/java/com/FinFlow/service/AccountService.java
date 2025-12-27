package com.FinFlow.service;

import com.FinFlow.domain.Account;
import com.FinFlow.domain.Transaction;
import com.FinFlow.domain.TransactionEnum;
import com.FinFlow.domain.User;
import com.FinFlow.dto.account.AccountReqDTO.AccountDepositReqDTO;
import com.FinFlow.dto.account.AccountReqDTO.AccountSaveReqDto;
import com.FinFlow.dto.account.AccountReqDTO.AccountWithdrawReqDTO;
import com.FinFlow.dto.account.AccountRespDTO.AccountDepositRespDTO;
import com.FinFlow.dto.account.AccountRespDTO.AccountListRespDTO;
import com.FinFlow.dto.account.AccountRespDTO.AccountSaveRespDto;
import com.FinFlow.dto.account.AccountRespDTO.AccountWithdrawRespDTO;
import com.FinFlow.handler.ex.CustomApiException;
import com.FinFlow.repository.AccountRepository;
import com.FinFlow.repository.TransactionRepository;
import com.FinFlow.repository.UserRepository;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

  private final UserRepository userRepository;
  private final AccountRepository accountRepository;
  private final TransactionRepository transactionRepository;

  @Transactional
  public AccountSaveRespDto registerAccount(AccountSaveReqDto accountSaveReqDto, Long userId) {
    // User가 DB에 있는지 검증
    User user = userRepository.findById(userId).orElseThrow(
            () -> new CustomApiException("유저를 찾을 수 없습니다.")
    );

    // 해당 계좌가 DB에 있는 중복 여부를 체크
    Optional<Account> accountOptional = accountRepository.findByNumber(accountSaveReqDto.getNumber());
    if (accountOptional.isPresent()) {
      throw new CustomApiException("해당 계좌가 이미 존재합니다.");
    }

    // 계좌 등록
    Account account = accountRepository.save(accountSaveReqDto.toEntity(user));

    // DTO를 응답
    return new AccountSaveRespDto(account);
  }

  public AccountListRespDTO findAccountsByUser(Long userId) {
    // User가 DB에 있는지 검증
    User user = userRepository.findById(userId).orElseThrow(
            () -> new CustomApiException("유저를 찾을 수 없습니다.")
    );

    // 유저의 모든 계좌목록
    List<Account> accountList = accountRepository.findByUserId(userId);

    return new AccountListRespDTO(user, accountList);
  }

  @Transactional
  public void deleteAccount(String number, Long userId) {
    // 1. Check if the account exists
    Account account = accountRepository.findByNumber(number).orElseThrow(
            () -> new CustomApiException("계좌를 찾을 수 없습니다.")
    );

    // 2. Verify account ownership
    account.checkOwner(userId);

    // 3. Delete the account
    accountRepository.deleteById(account.getId());
  }

  @Transactional
  public AccountDepositRespDTO depositAccount(AccountDepositReqDTO accountDepositReqDTO) {
    // Check for zero amount
    if (accountDepositReqDTO.getAmount() <= 0L) {
      throw new CustomApiException("0원 이하의 금액을 입금할 수 없습니다.");
    }

    // Validate deposit account
    Account depositAccount = accountRepository.findByNumber(accountDepositReqDTO.getNumber()).orElseThrow(
            () -> new CustomApiException("계좌를 찾을 수 없습니다.")
    );

    // Deposit funds
    depositAccount.deposit(accountDepositReqDTO.getAmount());

    // Record transaction history
    Transaction transaction = Transaction.builder()
            .depositAccount(depositAccount)
            .withdrawAccount(null)
            .depositAccountBalance(depositAccount.getBalance())
            .amount(accountDepositReqDTO.getAmount())
            .transaction_type(TransactionEnum.DEPOSIT)
            .sender("ATM")
            .receiver(accountDepositReqDTO.getNumber() + "")
            .tel(accountDepositReqDTO.getTel())
            .build();

    Transaction saveTransaction = transactionRepository.save(transaction);

    // Return DTO response
    return new AccountDepositRespDTO(depositAccount, saveTransaction);
  }

  @Transactional
  public AccountWithdrawRespDTO withdrawAccount(AccountWithdrawReqDTO accountWithdrawReqDTO, Long userId) {
    // Check for zero amount
    if (accountWithdrawReqDTO.getAmount() <= 0L) {
      throw new CustomApiException("0원 이하의 금액을 입금할 수 없습니다.");
    }

    // Validate withdraw account(repository)
    Account withdrawAccount = accountRepository.findByNumber(accountWithdrawReqDTO.getNumber()).orElseThrow(
            () -> new CustomApiException("계좌를 찾을 수 없습니다.")
    );

    // Verify withdraw account ownership(matches the logged-in user)
    withdrawAccount.checkOwner(userId);

    // Verify withdraw account password
    withdrawAccount.checkSamePassword(accountWithdrawReqDTO.getPassword());

    // Check withdraw account balance
    withdrawAccount.checkBalance(accountWithdrawReqDTO.getAmount());

    // Withdraw amount
    withdrawAccount.withdraw(accountWithdrawReqDTO.getAmount());

    // Record transaction history(repository)
    Transaction transaction = Transaction.builder()
            .withdrawAccount(withdrawAccount)
            .depositAccount(null)
            .withdrawAccountBalance(withdrawAccount.getBalance())
            .depositAccountBalance(null)
            .amount(accountWithdrawReqDTO.getAmount())
            .transaction_type(TransactionEnum.WITHDRAW)
            .sender(accountWithdrawReqDTO.getNumber() + "")
            .receiver("ATM")
            .build();

    Transaction saveTransaction = transactionRepository.save(transaction);

    // Return DTO response
    return new AccountWithdrawRespDTO(withdrawAccount, saveTransaction);
  }
}
