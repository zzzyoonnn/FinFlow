package com.FinFlow.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.FinFlow.config.dummy.DummyObject;
import com.FinFlow.domain.Account;
import com.FinFlow.domain.Transaction;
import com.FinFlow.domain.User;
import com.FinFlow.dto.account.AccountReqDTO.AccountDepositReqDTO;
import com.FinFlow.dto.account.AccountReqDTO.AccountSaveReqDto;
import com.FinFlow.dto.account.AccountRespDTO.AccountDepositRespDTO;
import com.FinFlow.dto.account.AccountRespDTO.AccountListRespDTO;
import com.FinFlow.dto.account.AccountRespDTO.AccountSaveRespDto;
import com.FinFlow.handler.ex.CustomApiException;
import com.FinFlow.repository.AccountRepository;
import com.FinFlow.repository.TransactionRepository;
import com.FinFlow.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
 public class AccountServiceTests extends DummyObject {

  @InjectMocks  // 모든 Mock들이 InjectMocks로 주입됨
  private AccountService accountService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private AccountRepository accountRepository;

  @Mock
  private TransactionRepository transactionRepository;

  @Spy
  private ObjectMapper objectMapper;

  @Test
  public void registerAccount_test() throws Exception {
    // given
    Long userId = 1L;

    AccountSaveReqDto accountSaveReqDto = new AccountSaveReqDto();
    accountSaveReqDto.setNumber("1234567890");
    accountSaveReqDto.setPassword(1234L);

    // stub 1
    User testUser = newMockUser(userId, "test", "test");
    when (userRepository.findById(any())).thenReturn(Optional.of(testUser));

    // stub 2
    when (accountRepository.findByNumber(any())).thenReturn(Optional.empty());

    // stub 3
    Account testAccount = newMockAccount(1L, "1234567890", 1000L, testUser);
    when (accountRepository.save(any())).thenReturn(testAccount);

    // when
    AccountSaveRespDto accountSaveRespDto = accountService.registerAccount(accountSaveReqDto, userId);
    String responseBody = objectMapper.writeValueAsString(accountSaveRespDto);

    // then
    assertThat(accountSaveRespDto.getNumber()).isEqualTo("1234567890");
  }

  @Test
  public void findAccountsByUser_test() throws Exception {
    // given
    Long userId = 1L;

    // stub
    User testUser = newMockUser(userId, "test", "test");
    when (userRepository.findById(any())).thenReturn(Optional.of(testUser));

    Account testAccount1 = newMockAccount(1L, "1111111111", 1000L, testUser);
    Account testAccount2 = newMockAccount(2L, "2222222222", 1000L, testUser);
    List<Account> accountList = Arrays.asList(testAccount1, testAccount2);

    when (accountRepository.findByUserId(any())).thenReturn(accountList);

    // when
    AccountListRespDTO accountListRespDTO = accountService.findAccountsByUser(userId);

    // then
    assertThat(accountListRespDTO.getFullname()).isEqualTo("test");
    assertThat(accountListRespDTO.getAccountList().size()).isEqualTo(2);
  }

  @Test
  public void deleteAccount_test() throws Exception {
    // given
    String number = "1234567890";
    Long userId = 2L;

    // stub
    User testUser = newMockUser(1L, "test", "test");
    Account testAccount = newMockAccount(1L, "1234567890", 1000L, testUser);
    when (accountRepository.findByNumber(any())).thenReturn(Optional.of(testAccount));

    // when & then
    assertThrows(CustomApiException.class, () -> accountService.deleteAccount(number, userId));
  }

  @Test
  public void depositAccount_test() throws Exception {
    // given
    AccountDepositReqDTO accountDepositReqDTO = new AccountDepositReqDTO();
    accountDepositReqDTO.setNumber("1111111111");
    accountDepositReqDTO.setAmount(500L);
    accountDepositReqDTO.setTransactionType("DEPOSIT");
    accountDepositReqDTO.setTel("010-1111-1111");

    // stub 1
    User testUser = newMockUser(1L, "test", "test");
    Account testAccount = newMockAccount(1L, "1111111111", 1000L, testUser);
    when (accountRepository.findByNumber(any())).thenReturn(Optional.of(testAccount));

    Account testAccount2 = newMockAccount(1L, "1111111111", 1000L, testUser);
    Transaction transaction = newMockDepositTransaction(1L, testAccount2);
    when (transactionRepository.save(any())).thenReturn(transaction);

    // when
    AccountDepositRespDTO accountDepositRespDTO = accountService.depositAccount(accountDepositReqDTO);
    System.out.println("트랜잭션 입금 계좌 잔액 : " + accountDepositRespDTO.getTransaction().getDepositAccountBalance());
    System.out.println("계좌 잔액 : " + testAccount.getBalance());

    // then
    assertThat(testAccount.getBalance()).isEqualTo(1500L);
    assertThat(accountDepositRespDTO.getTransaction().getDepositAccountBalance()).isEqualTo(1500L);
  }

  @Test // dto 값 검증
  public void depositAccount_test2() throws Exception {
    // given
    AccountDepositReqDTO accountDepositReqDTO = new AccountDepositReqDTO();
    accountDepositReqDTO.setNumber("1111111111");
    accountDepositReqDTO.setAmount(500L);
    accountDepositReqDTO.setTransactionType("DEPOSIT");
    accountDepositReqDTO.setTel("010-1111-1111");

    // stub 1
    User testUser = newMockUser(1L, "test", "test");// 실행
    Account testAccount = newMockAccount(1L, "1111111111", 1000L, testUser);
    when (accountRepository.findByNumber(any())).thenReturn(Optional.of(testAccount));

    // stub 2
    User testUser2 = newMockUser(1L, "test", "test");// 실행
    Account testAccount2 = newMockAccount(1L, "1111111111", 1000L, testUser2);// 실행 1000원
    Transaction transaction = newMockDepositTransaction(1L, testAccount2);// 실행 1500(test 1500, transaction 1500;
    when (transactionRepository.save(any())).thenReturn(transaction);

    // when
    AccountDepositRespDTO accountDepositRespDTO = accountService.depositAccount(accountDepositReqDTO);
    String responseBody = objectMapper.writeValueAsString(accountDepositRespDTO);
    System.out.println(responseBody);

    //  then
    assertThat(testAccount.getBalance()).isEqualTo(1500L);
  }

  @Test // dto 값 검증
  public void depositAccount_test3() throws Exception {
    // given
    Account testAccount = newMockAccount(1L, "1111111111", 1000L, null);
    Long amount = 10L;

    // when
    if (amount <= 0L) {
      throw new CustomApiException("0원 이하의 금액을 입금할 수 없습니다.");
    }
    testAccount.deposit(500L);

    // then
    assertThat(testAccount.getBalance()).isEqualTo(1500L);
  }

  // 계좌 출금_테스트(서비스)
  // 계좌 이체_테스트(서비스)
  // 계좌목록보기_유저별_테스트(서비스)
  // 계좌상세보기_테스트(서비스)
  // 대부분 컨트롤러에서 테스트 가능. 정말 필요한 테스트일까?
  // 서비스 테스트는 DB 동작이나 DTO 검증이 아니라, DB 조회 결과를 기반으로 비즈니스 로직이 올바르게 동작하는지를 검증하는 데 목적이 있다.

}
