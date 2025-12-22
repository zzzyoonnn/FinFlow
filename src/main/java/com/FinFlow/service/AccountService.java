package com.FinFlow.service;

import com.FinFlow.domain.Account;
import com.FinFlow.domain.User;
import com.FinFlow.dto.account.AccountReqDTO.AccountSaveReqDto;
import com.FinFlow.dto.account.AccountRespDTO.AccountListRespDTO;
import com.FinFlow.dto.account.AccountRespDTO.AccountSaveRespDto;
import com.FinFlow.handler.ex.CustomApiException;
import com.FinFlow.repository.AccountRepository;
import com.FinFlow.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

  private final UserRepository userRepository;
  private final AccountRepository accountRepository;

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
}
