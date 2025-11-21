package com.FinFlow.config.jwt;

import com.FinFlow.config.auth.LoginUser;
import com.FinFlow.dto.user.UserReqDto.JoinReqDto.LoginReqDto;
import com.FinFlow.dto.user.UserRespDto.LoginRespDto;
import com.FinFlow.util.CustomResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private AuthenticationManager authenticationManager;

  public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
    super(authenticationManager);
    setFilterProcessesUrl("/api/login");
    this.authenticationManager = authenticationManager;
  }

  // POST : /api/login
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
          throws AuthenticationException {

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
      Authentication authentication = authenticationManager.authenticate(authenticationToken);
      return authentication;

    } catch (Exception e) {
      // authenticationEntryPoint에 걸린다.
      throw new InternalAuthenticationServiceException(e.getMessage());
    }
  }

  // return authentication 잘 작동하면 successfulAuthentication 메서드 호출
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                          Authentication authResult) throws IOException, ServletException {
    LoginUser loginUser = (LoginUser) authResult.getPrincipal();
    String jwtToken = JwtProcess.create(loginUser);
    response.addHeader(JwtVO.HEADER, jwtToken);

    LoginRespDto loginRespDto = new LoginRespDto(loginUser.getUser());
    CustomResponseUtil.successAuthentication(response, loginRespDto);
  }
}
