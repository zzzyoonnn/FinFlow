package com.FinFlow.config.jwt;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.FinFlow.config.auth.LoginUser;
import com.FinFlow.domain.User;
import com.FinFlow.domain.UserEnum;
import org.junit.jupiter.api.Test;

public class JwtProcessTests {

  @Test
  public void create_test() throws Exception {
    // given
    User user = User.builder().id(1L).role(UserEnum.CUSTOMER).build();
    LoginUser loginUser = new LoginUser(user);

    // when
    String jwtToken = JwtProcess.create(loginUser);

    // then
    assertTrue(jwtToken.startsWith(JwtVO.TOKEN_PREFIX));
  }

  @Test
  public void verify_test() throws Exception {
    // given
    String jwtToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmaW5GbG93Iiwicm9sZSI6IkNVU1RPTUVSIiwiaWQiOjEsImV4cCI6MTc2NjE1MDU5N30.F07rpqc0mUQZulecr0QPZ9ZbIkeDha2lQPur26FmfXGbNEdCaS58TOkGKp6NoJbtksbA7R57LRH01KfjIOS80A";

    // when
    LoginUser loginUser = JwtProcess.verify(jwtToken);

    // then
    assertThat(loginUser.getUser().getId()).isEqualTo(1L);
    assertThat(loginUser.getUser().getRole()).isEqualTo(UserEnum.CUSTOMER);
  }
}
