package com.FinFlow.dto.user;

import com.FinFlow.domain.User;
import com.FinFlow.domain.UserEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserReqDto {

  @Setter
  @Getter
  public static class JoinReqDto {
    private String username;
    private String password;
    private String email;
    private String fullname;

    public User toEntity(BCryptPasswordEncoder bCryptPasswordEncoder) {
      return User.builder()
              .username(username)
              .password(bCryptPasswordEncoder.encode(password))
              .email(email)
              .fullname(fullname)
              .role(UserEnum.CUSTOMER)
              .build();
    }
  }
}
