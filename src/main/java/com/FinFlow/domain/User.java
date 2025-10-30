package com.FinFlow.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "users")
@Entity
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false, length = 20)
  private String username;

  @Column(nullable = false)   // 패스워드 인코딩(BCrypt)
  private String password;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false, length = 40)
  private String fullname;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private UserEnum role;    // ADMIN, CUSTOMER

  @CreatedDate
  @Column(nullable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(nullable = false)
  private LocalDateTime updatedAt;

  @Builder
  public User(Long id, String username, String password, String email, String fullname, UserEnum role, LocalDateTime updatedAt, LocalDateTime createdAt) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.email = email;
    this.fullname = fullname;
    this.role = role;
    this.updatedAt = updatedAt;
    this.createdAt = createdAt;
  }
}
