package com.FinFlow.config;

import com.FinFlow.domain.UserEnum;
import com.FinFlow.util.CustomResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// @Slf4j : 테스트 시 에러 발생
@Configuration
public class SecurityConfig {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Bean   // IoC 컨테이너에 BCryptPasswordEncoder() 객체가 등록됨
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // JWT 서버 사용(Session 사용 X)
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .headers(headers -> headers.frameOptions(frame -> frame.disable())) // iframe 허용
            .csrf(csrf -> csrf.disable()) // Postman 등 테스트용
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 허용
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용 안 함
            .formLogin(form -> form.disable()) // React, 앱용
            .httpBasic(basic -> basic.disable()) // 브라우저 인증 팝업 비활성화

            // Exception 가로채기
            .exceptionHandling(exception -> exception
                    .authenticationEntryPoint((request, response, authException) -> {
                      CustomResponseUtil.unAuthentication(response, "로그인을 진행해 주세요.");
                    })
            )

            .authorizeHttpRequests(auth -> auth //권한 설정
                    .requestMatchers("/api/s/**").authenticated()   // 로그인 필요
                    .requestMatchers("/api/admin/**").hasRole(UserEnum.ADMIN.name()) // ADMIN 권한 필요
                    .anyRequest().permitAll()   // 그 외는 모두 허용
            );

    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    configuration.addAllowedMethod("*");        // 모든 HTTP 메서드 허용
    configuration.addAllowedOriginPattern("*"); // 모든 도메인 허용
    configuration.addAllowedHeader("*");        // 모든 헤더 허용
    configuration.setAllowCredentials(true);    // 쿠키/인증정보 허용

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
