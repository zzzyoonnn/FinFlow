package com.FinFlow.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.FinFlow.config.dummy.DummyObject;
import com.FinFlow.domain.Account;
import com.FinFlow.domain.User;
import com.FinFlow.dto.account.AccountReqDTO.AccountSaveReqDto;
import com.FinFlow.dto.account.AccountRespDTO.AccountSaveRespDto;
import com.FinFlow.repository.AccountRepository;
import com.FinFlow.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
}
