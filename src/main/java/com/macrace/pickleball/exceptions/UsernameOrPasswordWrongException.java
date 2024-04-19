package com.macrace.pickleball.exceptions;

import com.macrace.pickleball.constant.ErrorCode;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@NotBlank
@Getter
public class UsernameOrPasswordWrongException extends RuntimeException {
    private Integer errorCode = ErrorCode.USERNAME_PASSWORD_WRONG;
    private String message = "Phone number or password incorrect";
}
