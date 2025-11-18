package com.FinFlow.config.auth;

import com.FinFlow.domain.User;
import com.FinFlow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  // 시큐리티로 로그인 될 때, 시큐리티가 loadUserByUsername() 실행해서 username 체크
  // 없으면 오류, 있으면 정상적으로 시큐리티 컨텍스트 내부 세션에 로그인된 세션 생성
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username).orElseThrow(
            () -> new UsernameNotFoundException("인증 실패: " + username) // 나중에 테스트
    );
    return new LoginUser(user);
  }
}
