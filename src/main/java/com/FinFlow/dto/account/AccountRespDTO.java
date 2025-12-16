package com.FinFlow.dto.account;

import com.FinFlow.domain.Account;
import lombok.Getter;
import lombok.Setter;

public class AccountRespDTO {

  @Getter
  @Setter
  public static class AccountSaveRespDto {
    private Long id;
    private String number;
    private Long balance;

    public AccountSaveRespDto(Account account) {
      this.id = account.getId();
      this.number = account.getNumber();
      this.balance = account.getBalance();
    }
  }
}
