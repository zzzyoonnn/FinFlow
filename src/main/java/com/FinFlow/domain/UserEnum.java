package com.FinFlow.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserEnum {
  ADMIN("관리자"), CUSTOMER("고객");

  private String value;
}
