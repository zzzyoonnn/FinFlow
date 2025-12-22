package com.FinFlow.dto.account;

import com.FinFlow.domain.Account;
import com.FinFlow.domain.User;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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

  @Getter
  @Setter
  public static class AccountListRespDTO {
    private String fullname;
    private List<AccountDTO> accountList = new ArrayList<>();

    public AccountListRespDTO(User user, List<Account> accountList) {
      this.fullname = user.getFullname();
      //this.accountList = accountList.stream().map((account) -> new AccountDTO(account)).collect(Collectors.toList()));
      this.accountList = accountList.stream().map(AccountDTO::new).collect(Collectors.toList());
      // [account, account, account, ...]
    }

    @Getter
    @Setter
    public class AccountDTO {
      private Long id;
      private String number;
      private Long balance;

      public AccountDTO(Account account) {
        this.id = account.getId();
        this.number = account.getNumber();
        this.balance = account.getBalance();
      }
    }
  }
}
