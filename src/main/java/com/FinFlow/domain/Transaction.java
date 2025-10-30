package com.FinFlow.domain;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "account_transaction")
@Entity
public class Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
  @ManyToOne(fetch = FetchType.LAZY)
  private Account withdrawAccount;  // 출금 계좌

  @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
  @ManyToOne(fetch = FetchType.LAZY)
  private Account depositAccount;   // 입금 계좌

  @Column(nullable = false)
  private Long amount;    // 금액

  private Long withdrawAccountBalance;  // 출금 후 잔액
  private Long depositAccountBalance;   // 입금 후 잔액

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private TransactionEnum transaction_type;

  @LastModifiedDate
  @Column(nullable = false)
  private LocalDateTime updatedAt;

  @CreatedDate
  @Column(nullable = false)
  private LocalDateTime createdAt;

  // 계좌가 사라져도 로그는 남아야 함
  private String sender;
  private String receiver;
  private String tel;

  @Builder
  public Transaction(Long id, Account withdrawAccount, Account depositAccount, Long amount, Long withdrawAccountBalance, Long depositAccountBalance, TransactionEnum transaction_type, LocalDateTime updatedAt, LocalDateTime createdAt, String sender, String receiver, String tel) {
    this.id = id;
    this.withdrawAccount = withdrawAccount;
    this.depositAccount = depositAccount;
    this.amount = amount;
    this.withdrawAccountBalance = withdrawAccountBalance;
    this.depositAccountBalance = depositAccountBalance;
    this.transaction_type = transaction_type;
    this.updatedAt = updatedAt;
    this.createdAt = createdAt;
    this.sender = sender;
    this.receiver = receiver;
    this.tel = tel;
  }
}
