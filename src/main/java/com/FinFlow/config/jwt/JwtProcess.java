package com.FinFlow.config.jwt;

import com.FinFlow.config.auth.LoginUser;
import com.FinFlow.domain.User;
import com.FinFlow.domain.UserEnum;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JwtProcess {
  private final Logger log = LoggerFactory.getLogger(getClass());

  public static String create(LoginUser loginUser) {
    String jwtToken = JWT.create()
            .withSubject("finFlow")
            .withExpiresAt(new Date(System.currentTimeMillis() + JwtVO.EXPIRATION_TIME))
            .withClaim("id", loginUser.getUser().getId())
            .withClaim("role", loginUser.getUser().getRole() + "")
            .sign(Algorithm.HMAC512(JwtVO.SECRET));

    return JwtVO.TOKEN_PREFIX + jwtToken;
  }

  // 토큰 검증 (return 되는 LoginUser 객체를 강제로 시큐리티 세션에 직접 주입할 예정)
  public static LoginUser verify(String token) {
    DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(JwtVO.SECRET)).build().verify(token);
    Long id = decodedJWT.getClaim("id").asLong();
    String role = decodedJWT.getClaim("role").asString();
    User user = User.builder().id(id).role(UserEnum.valueOf(role)).build();
    LoginUser loginUser = new LoginUser(user);

    return loginUser;
  }
}
