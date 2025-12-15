package com.FinFlow.temp;

import java.util.regex.Pattern;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RegexTests {

  @Test
  @DisplayName("한글만 된다")
  public void shouldAllowOnlyKoreanCharacters() throws Exception {
    String value = "가나다";
    boolean result = Pattern.matches("^[ㄱ-ㅎ가-힣]+$", value);
    System.out.println("테스트: " + result);
  }

  @Test
  @DisplayName("한글은 안된다")
  public void shouldNotAllowKoreanCharacters() throws Exception {
    String value = "기";
    boolean result = Pattern.matches("^[^ㄱ-ㅎ가-힣]*$", value);
    System.out.println("테스트: " + result);
  }

  @Test
  @DisplayName("영어만 된다")
  public void shouldAllowOnlyEnglishLetters() throws Exception {
    String value = "test";
    boolean result = Pattern.matches("^[a-zA-Z]+$", value);
    System.out.println("테스트: " + result);
  }

  @Test
  @DisplayName("영어는 안된다")
  public void shouldNotAllowEnglishLetters() throws Exception {
    String value = "test";
    boolean result = Pattern.matches("^[^a-zA-Z]*$", value);
    System.out.println("테스트: " + result);
  }

  @Test
  @DisplayName("영어와 숫자만 된다")
  public void shouldAllowEnglishLettersAndNumbers() throws Exception {
    String value = "test123";
    boolean result = Pattern.matches("^[a-zA-Z0-9]+$", value);
    System.out.println("테스트: " + result);
  }

  @Test
  @DisplayName("영어만 되고 길이는 최소2 최대4")
  public void shouldAllowOnlyEnglishLettersWithLengthBetween2And4() throws Exception {
    String value = "test123";
    boolean result = Pattern.matches("^[a-zA-Z]{2,4}$", value);
    System.out.println("테스트: " + result);
  }

  @Test
  @DisplayName("Username 테스트")
  public void shouldPassWhenUsernameIsValid() throws Exception {
    String username = "testUser";
    boolean result = Pattern.matches("^[a-zA-Z0-9]{2,20}$", username);
    System.out.println("테스트: " + result);
  }

  @Test
  @DisplayName("Fullname 테스트")
  public void shouldPassWhenFullnameIsValid() throws Exception {
    String fullname = "테스트";
    boolean result = Pattern.matches("^[a-zA-Z가-힣]{1,20}$", fullname);
    System.out.println("테스트: " + result);
  }

  @Test
  @DisplayName("Email 테스트")
  public void shouldPassWhenEmailIsValid() throws Exception {
    String value = "test@test.com";
    boolean result = Pattern.matches("^[a-zA-Z0-9]+@[a-zA-Z0-9]{2,4}\\.[a-zA-Z]{2,3}$", value);
    System.out.println("테스트: " + result);
  }
}
