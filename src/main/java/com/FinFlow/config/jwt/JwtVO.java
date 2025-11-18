package com.FinFlow.config.jwt;

public class JwtVO {
  public static final String SECRET = "";
  public static final int EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7;  // 일주일
  public static final String TOKEN_PREFIX = "Bearer ";
  public static final String HEADER = "Authorization";
}
