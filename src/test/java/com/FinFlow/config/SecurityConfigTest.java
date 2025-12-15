package com.FinFlow.config;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@AutoConfigureMockMvc   // Mock 환경에 MockMvc가 등록
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class SecurityConfigTest {

  // 가짜 환경에 등록된 MockMvc를 DI
  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("서버는 일관성있게 에러가 리턴되어야 한다.")
  public void authentication_test() throws Exception {
    // given

    // when
    ResultActions resultActions = mockMvc.perform(get("/api/s/hello"));
    String responseBody = resultActions.andReturn().getResponse().getContentAsString();
    int httpStatusCode = resultActions.andReturn().getResponse().getStatus();
    System.out.println("test: " + responseBody);
    System.out.println("test: " + httpStatusCode);

    // then
    assertThat(httpStatusCode).isEqualTo(401);
  }

  @Test
  public void authorization_test() throws Exception {
    // given

    // when
    ResultActions resultActions = mockMvc.perform(get("/api/admin/hello"));
    String responseBody = resultActions.andReturn().getResponse().getContentAsString();
    int httpStatusCode = resultActions.andReturn().getResponse().getStatus();
    System.out.println("test: " + responseBody);
    System.out.println("test: " + httpStatusCode);

    // then
    assertThat(httpStatusCode).isEqualTo(401);
  }
}
