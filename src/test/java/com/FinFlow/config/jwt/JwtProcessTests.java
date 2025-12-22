package com.FinFlow.config.jwt;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.FinFlow.config.auth.LoginUser;
import com.FinFlow.domain.User;
import com.FinFlow.domain.UserEnum;
import org.junit.jupiter.api.Test;

public class JwtProcessTests {

  public String createToken() {
    User user = User.builder().id(1L).role(UserEnum.CUSTOMER).build();
    LoginUser loginUser = new LoginUser(user);

    String jwtToken = JwtProcess.create(loginUser);

    return jwtToken;
  }

  @Test
  public void create_test() throws Exception {
    // given
    String jwtToken = createToken();

    // then
    assertTrue(jwtToken.startsWith(JwtVO.TOKEN_PREFIX));
  }

  @Test
  public void verify_test() throws Exception {
    // given
    String token = createToken();
    String jwtToken = token.replace(JwtVO.TOKEN_PREFIX, "");

    // when
    LoginUser loginUser = JwtProcess.verify(jwtToken);

    // then
    assertThat(loginUser.getUser().getId()).isEqualTo(1L);
    assertThat(loginUser.getUser().getRole()).isEqualTo(UserEnum.CUSTOMER);
  }
}
