package com.FinFlow.util;

import com.FinFlow.dto.ResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

public class CustomResponseUtil {
  private static final Logger log = LoggerFactory.getLogger(CustomResponseUtil.class);

  public static void fail(HttpServletResponse response, String message, HttpStatus httpStatus) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      ResponseDTO<?> responseDTO = new ResponseDTO<>(-1, message, null);
      String responseBody = objectMapper.writeValueAsString(responseDTO);
      response.setStatus(httpStatus.value());
      response.setContentType("application/json;charset=UTF-8");
      response.getWriter().println(responseBody);

    } catch (Exception e) {
      log.error(e.getMessage(), "서버 파싱 에러: " + e);
    }
  }

  public static void successAuthentication(HttpServletResponse response, Object dto) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      ResponseDTO<?> responseDTO = new ResponseDTO<>(1, "로그인 성공", dto);
      String responseBody = objectMapper.writeValueAsString(responseDTO);
      response.setStatus(200);
      response.setContentType("application/json;charset=UTF-8");
      response.getWriter().println(responseBody);

    } catch (Exception e) {
      log.error(e.getMessage(), "서버 파싱 에러: " + e);
    }
  }
}
