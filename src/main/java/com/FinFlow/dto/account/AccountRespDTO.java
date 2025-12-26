package com.FinFlow.dto.account;

import com.FinFlow.domain.Account;
import com.FinFlow.domain.Transaction;
import com.FinFlow.domain.User;
import com.FinFlow.util.CustomDateUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

  @Getter
  @Setter
  public static class AccountDepositRespDTO {
    private Long id;
    private String number;
    private TransactionDTO transaction;

    public AccountDepositRespDTO(Account account, Transaction transaction) {
      this.id = account.getId();
      this.number = account.getNumber();
      this.transaction = new TransactionDTO(transaction);
    }

    @Getter
    @Setter
    public class TransactionDTO {
      private Long id;
      private String transactionType;
      private String sender;
      private String receiver;
      private Long amount;

      @JsonIgnore
      private Long depositAccountBalance;   // 클라이언트에게 전달 X(서비스단에서 테스트 용도)
      private String tel;
      private String createdAt;


      public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.transactionType = transaction.getTransaction_type().getValue();
        this.sender = transaction.getSender();
        this.receiver = transaction.getReceiver();
        this.amount = transaction.getAmount();
        this.depositAccountBalance = transaction.getDepositAccountBalance();
        this.tel = transaction.getTel();
        this.createdAt = CustomDateUtil.toStringFormat(transaction.getCreatedAt());
      }
    }
  }
}
