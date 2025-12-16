package com.FinFlow.dto.account;

import com.FinFlow.domain.Account;
import com.FinFlow.domain.User;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public class AccountReqDTO {

  @Getter
  @Setter
  public static class AccountSaveReqDto {

    @NotBlank
    @Size(min = 10, max = 10)
    @Pattern(regexp = "\\d+")
    private String number;

    @NotNull
    @Digits(integer = 4, fraction = 4)
    private Long password;

    public Account toEntity(User user) {
      return Account.builder()
              .number(number)
              .password(password)
              .balance(1000L)
              .user(user)
              .build();
    }
  }
}
