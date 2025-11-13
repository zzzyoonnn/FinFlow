package com.FinFlow.handler.aop;

import com.FinFlow.handler.ex.CustomValidationException;
import java.util.HashMap;
import java.util.Map;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Aspect
@Component
public class CustomValidationAdvice {

  // get, delete, post(body), put(body)

  @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
  public void postMapping() {}

  @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
  public void putMapping() {}

  @Around("postMapping() || putMapping()")  // joinPoint의 전후 제어
  public Object validationAdvice(ProceedingJoinPoint joinPoint) throws Throwable {

    Object[] args = joinPoint.getArgs();  // joinPoint의 매개변수

    for (Object arg : args) {
      if (arg instanceof BindingResult) {
        BindingResult bindingResult = (BindingResult) arg;

        if (bindingResult.hasErrors()) {
          Map<String, String> errorMap = new HashMap<>();

          for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
          }

          throw new CustomValidationException("유효성 검사 실패", errorMap);
        }
      }
    }

    return joinPoint.proceed(); // 정상적으로 해당 메서드 실행
  }
}
