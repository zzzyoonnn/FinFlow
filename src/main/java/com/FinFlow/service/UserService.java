package com.FinFlow.service;

import com.FinFlow.domain.User;
import com.FinFlow.dto.user.UserReqDto.JoinReqDto;
import com.FinFlow.dto.user.UserRespDto.JoinRespDto;
import com.FinFlow.handler.ex.CustomApiException;
import com.FinFlow.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final Logger log = LoggerFactory.getLogger(getClass());
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  // 서비스는 DTO로 요청받고, DTO로 응답
  @Transactional  // 트랜잭션이 메서드 시작할 때, 시작되고 종료될 때 함께 종료
  public JoinRespDto signUp(JoinReqDto joinReqDto) {
    // 1. Validate duplicate username
    Optional<User> userOptional = userRepository.findByUsername(joinReqDto.getUsername());
    if (userOptional.isPresent()) {
      throw new CustomApiException("동일한 username 존재");
    }

    // 2. Encode the password
    User user = userRepository.save(joinReqDto.toEntity(bCryptPasswordEncoder));

    // 3. Return the response DTO
    return new JoinRespDto(user);
  }
}
