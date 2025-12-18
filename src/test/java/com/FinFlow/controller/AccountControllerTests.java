package com.FinFlow.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.FinFlow.config.dummy.DummyObject;
import com.FinFlow.domain.User;
import com.FinFlow.dto.account.AccountReqDTO.AccountSaveReqDto;
import com.FinFlow.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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

  @BeforeEach
  public void setUp() throws Exception {
    User user = userRepository.save(newUser("test", "test"));
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

    // then
    resultActions.andExpect(status().isOk());
  }
}
