package com.FinFlow.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResponseDTO<T> {

  private final Integer code;   // 1: 성공, -1: 실패
  private final String message;

  // 받을 수 있는 정상 메시지잉ㄹ 수도, 에러 메시지일 수도 있기에 제너릭으로 표현
  private final T data;
}
