package com.FinFlow.config.jwt;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.FinFlow.config.auth.LoginUser;
import com.FinFlow.domain.User;
import com.FinFlow.domain.UserEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class JwtAuthorizationFilterTests {

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void authorization_success_test() throws Exception {
    // given
    User user = User.builder().id(1L).role(UserEnum.CUSTOMER).build();
    LoginUser loginUser = new LoginUser(user);
    String jwtToken = JwtProcess.create(loginUser);

    // when
    ResultActions resultActions = mockMvc.perform(get("/api/s/hello/test").header(JwtVO.HEADER, jwtToken));

    // then
    resultActions.andExpect(status().isNotFound());
  }

  @Test
  public void authorization_fail_test() throws Exception {
    // given

    // when
    ResultActions resultActions = mockMvc.perform(get("/api/s/hello/test"));

    // then
    resultActions.andExpect(status().isUnauthorized());
  }

  @Test
  public void authorization_admin_test() throws Exception {
    // given
    User user = User.builder().id(1L).role(UserEnum.ADMIN).build();
    LoginUser loginUser = new LoginUser(user);
    String jwtToken = JwtProcess.create(loginUser);

    // when
    ResultActions resultActions = mockMvc.perform(get("/api/api/hello/test").header(JwtVO.HEADER, jwtToken));

    // then
    resultActions.andExpect(status().isNotFound());
  }
}
