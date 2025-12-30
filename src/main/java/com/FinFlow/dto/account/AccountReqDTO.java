package com.FinFlow.dto.account;

import com.FinFlow.domain.Account;
import com.FinFlow.domain.User;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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

  @Getter
  @Setter
  public static class AccountDepositReqDTO {

    @NotNull
    @Column(unique = true, nullable = false, length = 10)
    private String number;

    @NotNull
    private Long amount;

    @NotEmpty
    @Pattern(regexp = "^(DEPOSIT)$")
    private String transactionType;

    @NotEmpty
    @Pattern(regexp = "^[0-9]{3}-[0-9]{4}-[0-9]{4}")
    private String tel;
  }

  @Getter
  @Setter
  public static class AccountWithdrawReqDTO {

    @NotNull
    @Column(unique = true, nullable = false, length = 10)
    private String number;

    @NotNull
    @Digits(integer = 4, fraction = 4)
    private Long password;

    @NotNull
    private Long amount;

    @NotEmpty
    @Pattern(regexp = "^(WITHDRAW)$")
    private String transactionType;
  }

  @Getter
  @Setter
  public static class AccountTransferReqDTO {

    @NotNull
    @Column(unique = true, nullable = false, length = 10)
    private String withdrawNumber;

    @NotNull
    @Column(unique = true, nullable = false, length = 10)
    private String depositNumber;

    @NotNull
    @Digits(integer = 4, fraction = 4)
    private Long withdrawPassword;

    @NotNull
    private Long amount;

    @NotEmpty
    @Pattern(regexp = "^(TRANSFER)$")
    private String transactionType;
  }
}
