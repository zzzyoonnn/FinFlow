package com.FinFlow.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.FinFlow.config.dummy.DummyObject;
import com.FinFlow.domain.Account;
import com.FinFlow.domain.User;
import com.FinFlow.dto.account.AccountReqDTO.AccountSaveReqDto;
import com.FinFlow.handler.ex.CustomApiException;
import com.FinFlow.repository.AccountRepository;
import com.FinFlow.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class AccountControllerTests extends DummyObject {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private EntityManager entityManager;

  @BeforeEach
  public void setUp() throws Exception {
    User test = userRepository.save(newUser("test", "test"));
    User test2 = userRepository.save(newUser("test2", "test2"));
    Account testAccount = accountRepository.save(newAccount("1111111111", test));
    Account test2Account = accountRepository.save(newAccount("2222222222", test2));

    entityManager.clear();
  }

  @Test
  @WithUserDetails(value = "test", setupBefore = TestExecutionEvent.TEST_EXECUTION)
  public void saveAccount_test() throws Exception {
    // given
    AccountSaveReqDto accountSaveReqDto = new AccountSaveReqDto();
    accountSaveReqDto.setNumber("9999999999");
    accountSaveReqDto.setPassword(1234L);
    String requestBody = objectMapper.writeValueAsString(accountSaveReqDto);
    System.out.println(requestBody);

    // when
    ResultActions resultActions = mockMvc.perform(post("/api/s/account").content(requestBody).contentType(
            MediaType.APPLICATION_JSON));
    String responseBody = resultActions.andReturn().getResponse().getContentAsString();
    System.out.println(responseBody);

    // then
    resultActions.andExpect(status().isCreated());
  }

  @Test
  @WithUserDetails(value = "test", setupBefore = TestExecutionEvent.TEST_EXECUTION)
  public void findUserAccount_test() throws Exception {
    // given

    // when
    ResultActions resultActions = mockMvc.perform(get("/api/s/account/loginUser"));
    String responseBody = resultActions.andReturn().getResponse().getContentAsString();
    System.out.println(responseBody);

    // then
    resultActions.andExpect(status().isOk());
  }

  @Test
  @WithUserDetails(value = "test", setupBefore = TestExecutionEvent.TEST_EXECUTION)
  public void deleteAccount_test() throws Exception {
    // given
    String number = "1111111111";
//    String number = "2222222222";

    // when
    ResultActions resultActions = mockMvc.perform(delete("/api/s/account/" + number));
    String responseBody = resultActions.andReturn().getResponse().getContentAsString();
    System.out.println(responseBody);

    // then
    assertThrows(CustomApiException.class, () -> accountRepository.findByNumber(number).orElseThrow(
            () -> new CustomApiException("계좌를 찾을 수 없습니다.")
    ));
  }
}
