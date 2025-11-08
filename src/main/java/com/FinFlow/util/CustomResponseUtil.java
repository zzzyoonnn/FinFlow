package com.FinFlow.util;

import com.FinFlow.dto.ResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomResponseUtil {
  private static final Logger log = LoggerFactory.getLogger(CustomResponseUtil.class);

  public static void unAuthentication(HttpServletResponse response, String message) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      ResponseDTO<?> responseDTO = new ResponseDTO<>(-1, message, null);
      String responseBody = objectMapper.writeValueAsString(responseDTO);
      response.setStatus(401);
      response.setContentType("application/json;charset=UTF-8");
      response.getWriter().println(responseBody);

    } catch (Exception e) {
      log.error(e.getMessage(), "서버 파싱 에러: " + e);
    }
  }
}
