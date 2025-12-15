package com.FinFlow.controller;

import com.FinFlow.dto.ResponseDTO;
import com.FinFlow.dto.user.UserReqDto.JoinReqDto;
import com.FinFlow.dto.user.UserRespDto.JoinRespDto;
import com.FinFlow.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/join")
  public ResponseEntity<?> join(@RequestBody @Valid JoinReqDto joinReqDto, BindingResult bindingResult) {

    JoinRespDto joinRespDto = userService.signUp(joinReqDto);

    return new ResponseEntity<>(new ResponseDTO<>(1, "회원가입 성공", joinRespDto), HttpStatus.CREATED);
  }
}
