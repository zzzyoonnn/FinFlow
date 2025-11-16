package com.FinFlow.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.FinFlow.config.dummy.DummyObject;
import com.FinFlow.domain.User;
import com.FinFlow.dto.user.UserReqDto.JoinReqDto;
import com.FinFlow.repository.UserRepository;
import com.FinFlow.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class UserControllerTests extends DummyObject {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserService userService;
  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  public void setUp() throws Exception {
    dataSetting();
  }

  @Test
  public void join_success_test() throws Exception {
    // given
    JoinReqDto joinReqDto = new JoinReqDto();
    joinReqDto.setUsername("joinTest");
    joinReqDto.setPassword("1234");
    joinReqDto.setEmail("joinTest@test.com");
    joinReqDto.setFullname("회원가입테스트");

    String requestBody = objectMapper.writeValueAsString(joinReqDto);
    System.out.println("requestBody: " + requestBody);

    // when
    ResultActions resultActions = mockMvc.perform(post("/api/join").content(requestBody).contentType(MediaType.APPLICATION_JSON));
    String responseBody = resultActions.andReturn().getResponse().getContentAsString();
    System.out.println("responseBody: " + responseBody);

    // then
    resultActions.andExpect(status().isCreated());
  }

  @Test
  public void join_fail_test() throws Exception {
    // given
    JoinReqDto joinReqDto = new JoinReqDto();
    joinReqDto.setUsername("joinFailTest");
    joinReqDto.setPassword("1234");
    joinReqDto.setEmail("joinTest@test.com");
    joinReqDto.setFullname("회원가입실패테스트");

    String requestBody = objectMapper.writeValueAsString(joinReqDto);
    System.out.println("requestBody: " + requestBody);

    // when
    ResultActions resultActions = mockMvc.perform(post("/api/join").content(requestBody).contentType(MediaType.APPLICATION_JSON));
    String responseBody = resultActions.andReturn().getResponse().getContentAsString();
    System.out.println("responseBody: " + responseBody);

    // then
    resultActions.andExpect(status().isBadRequest());
  }

  private void dataSetting() throws Exception {
    userRepository.save(newUser("joinFailTest", "회원가입실패테스트"));
  }
}
