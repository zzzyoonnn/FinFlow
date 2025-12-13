package com.FinFlow.config.jwt;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.FinFlow.config.dummy.DummyObject;
import com.FinFlow.dto.user.UserReqDto.LoginReqDto;
import com.FinFlow.repository.UserRepository;
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
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class JwtAuthenticationFilterTests extends DummyObject {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  public void setUp() throws Exception {
    userRepository.save(newUser("test", "test"));
  }

  @Test
  public void successfulAuthentication_test() throws Exception {
    // given
    LoginReqDto loginReqDto = new LoginReqDto();
    loginReqDto.setUsername("test");
    loginReqDto.setPassword("1234");

    String requestBody = objectMapper.writeValueAsString(loginReqDto);
    System.out.println("test: " + requestBody);

    // when
    ResultActions resultActions = mockMvc.perform(post("/api/login").content(requestBody).contentType(
            MediaType.APPLICATION_JSON));
    String responseBody = resultActions.andReturn().getResponse().getContentAsString();
    String jwtToken = resultActions.andReturn().getResponse().getHeader(JwtVO.HEADER);

    // then
    resultActions.andExpect(status().isOk());
    assertNotNull(jwtToken);
    assertTrue(jwtToken.startsWith(JwtVO.TOKEN_PREFIX));
    resultActions.andExpect(jsonPath("$.data.username").value("test"));
  }

  @Test
  public void unsuccessfulAuthentication_test() throws Exception {
    // given
    LoginReqDto loginReqDto = new LoginReqDto();
    loginReqDto.setUsername("test");
    loginReqDto.setPassword("12345");

    String requestBody = objectMapper.writeValueAsString(loginReqDto);
    System.out.println("test: " + requestBody);

    // when
    ResultActions resultActions = mockMvc.perform(post("/api/login").content(requestBody).contentType(
            MediaType.APPLICATION_JSON));
    String responseBody = resultActions.andReturn().getResponse().getContentAsString();
    String jwtToken = resultActions.andReturn().getResponse().getHeader(JwtVO.HEADER);

    // then
    resultActions.andExpect(status().isUnauthorized());
//    assertNotNull(jwtToken);
//    assertTrue(jwtToken.startsWith(JwtVO.TOKEN_PREFIX));
//    resultActions.andExpect(jsonPath("$.data.username").value("test"));
  }
}
