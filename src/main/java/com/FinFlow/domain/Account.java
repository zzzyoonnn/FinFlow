package com.FinFlow.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "account")
@Entity
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false, length = 10)
  private String number;    // 계좌 번호

  @Column(nullable = false, length = 4)
  private Long password;  // 계좌 비밀번호

  @Column(nullable = false)
  private Long balance;   // 잔액

  @Column(nullable = false)
  private boolean isActive;   // 계좌 활성화 여부

  // 항상 ORM에서 fk의 주인은 Many Entity쪽
  @ManyToOne(fetch = FetchType.LAZY)  // account.getUser(); 아무필드 호출 시 Lazy 발동
  private User user;

  @LastModifiedDate
  @Column(nullable = false)
  private LocalDateTime updatedAt;

  @CreatedDate
  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Builder
  public Account(Long id, String number, Long password, Long balance, boolean isActive, User user, LocalDateTime updatedAt, LocalDateTime createdAt) {
    this.id = id;
    this.number = number;
    this.password = password;
    this.balance = balance;
    this.isActive = isActive;
    this.user = user;
    this.updatedAt = updatedAt;
    this.createdAt = createdAt;
  }
}
