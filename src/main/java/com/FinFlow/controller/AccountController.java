package com.FinFlow.controller;

import com.FinFlow.config.auth.LoginUser;
import com.FinFlow.dto.ResponseDTO;
import com.FinFlow.dto.account.AccountReqDTO.AccountSaveReqDto;
import com.FinFlow.dto.account.AccountRespDTO.AccountSaveRespDto;
import com.FinFlow.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccountController {

  private final AccountService accountService;

  @PostMapping("/s/account")
  public ResponseEntity<?> saveAccount(@RequestBody @Valid AccountSaveReqDto accountSaveReqDto,
                                       BindingResult bindingResult, @AuthenticationPrincipal LoginUser loginUser) {

    AccountSaveRespDto accountSaveRespDto = accountService.registerAccount(accountSaveReqDto, loginUser.getUser()
            .getId());
    return new ResponseEntity<>(new ResponseDTO<>(1, "계좌등록 성공", accountSaveRespDto), HttpStatus.CREATED);
  }
}
