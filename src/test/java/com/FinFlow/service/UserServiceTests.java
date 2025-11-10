package com.FinFlow.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.FinFlow.config.dummy.DummyObject;
import com.FinFlow.domain.User;
import com.FinFlow.domain.UserEnum;
import com.FinFlow.dto.user.UserReqDto.JoinReqDto;
import com.FinFlow.dto.user.UserRespDto.JoinRespDto;
import com.FinFlow.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// Spring 관련 Bean들이 하나도 없는 환경
@AutoConfigureMockMvc   // Mock 환경에 MockMvc가 등록
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class UserServiceTests extends DummyObject {

  @InjectMocks
  private UserService userService;

  @Mock
  private UserRepository userRepository;

  @Spy
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Test
  public void signUp_test() throws Exception {

    // given
    JoinReqDto joinReqDto = new JoinReqDto();
    joinReqDto.setUsername("test");
    joinReqDto.setPassword("1234");
    joinReqDto.setEmail("test@test.com");
    joinReqDto.setFullname("테스트");

    // stub 1
    when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

    // stub 2
    User testUser = newMockUser(1L, "test", "테스트");

    when(userRepository.save(any())).thenReturn(testUser);

    // when
    JoinRespDto joinRespDto = userService.signUp(joinReqDto);
    System.out.println(joinRespDto);

    // then
    assertThat(joinRespDto.getId()).isEqualTo(1L);
    assertThat(joinRespDto.getUsername()).isEqualTo("test");

  }
}
