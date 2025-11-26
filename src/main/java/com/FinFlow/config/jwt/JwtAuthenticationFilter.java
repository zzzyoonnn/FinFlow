package com.FinFlow.config.jwt;

import com.FinFlow.config.auth.LoginUser;
import com.FinFlow.dto.user.UserReqDto.LoginReqDto;
import com.FinFlow.dto.user.UserRespDto.LoginRespDto;
import com.FinFlow.util.CustomResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final Logger log = LoggerFactory.getLogger(getClass());
  private AuthenticationManager authenticationManager;

  public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
//    super(authenticationManager);
    setFilterProcessesUrl("/api/login");
    this.authenticationManager = authenticationManager;
  }

  // POST : /api/login
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
          throws AuthenticationException {
    log.debug("debug : attemptAuthentication 호출됨");
    try {
      ObjectMapper mapper = new ObjectMapper();
      LoginReqDto loginReqDto = mapper.readValue(request.getInputStream(), LoginReqDto.class);

      // 강제 로그인
      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
              loginReqDto.getUsername(), loginReqDto.getPassword()
      );

      // UserDetailsService의 loadUserByUsername 호출
      // JWT를 쓴다 하더라도, 컨트롤러 진입을 하면 시큐리티의 권한체크, 인증 체크의 도움을 받을 수 있게 세션을 만든다.
      // 이 세션의 유효기간은 request하고, response하면 끝
      // .with(new CustomSecurityFilterManager())
      Authentication authentication = authenticationManager.authenticate(authenticationToken);
      return authentication;

    } catch (Exception e) {
      // unsuccessfulAuthentication 호출
      throw new InternalAuthenticationServiceException(e.getMessage());
    }
  }

  // return authentication 잘 작동하면 successfulAuthentication 메서드 호출
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                          Authentication authResult) throws IOException, ServletException {

    log.debug("debug : successfulAuthentication 호출됨");
    LoginUser loginUser = (LoginUser) authResult.getPrincipal();
    String jwtToken = JwtProcess.create(loginUser);
    response.addHeader(JwtVO.HEADER, jwtToken);

    LoginRespDto loginRespDto = new LoginRespDto(loginUser.getUser());
    CustomResponseUtil.successAuthentication(response, loginRespDto);
  }

  // 로그인 실패
  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed
  ) throws IOException, ServletException {
    CustomResponseUtil.unAuthentication(response, "로그인 실패");
  }
}
